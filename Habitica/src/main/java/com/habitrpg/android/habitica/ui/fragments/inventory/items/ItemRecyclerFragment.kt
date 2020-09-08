package com.habitrpg.android.habitica.ui.fragments.inventory.items

import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.TextView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.habitrpg.android.habitica.R
import com.habitrpg.android.habitica.components.UserComponent
import com.habitrpg.android.habitica.data.InventoryRepository
import com.habitrpg.android.habitica.data.UserRepository
import com.habitrpg.android.habitica.databinding.FragmentItemsBinding
import com.habitrpg.android.habitica.databinding.FragmentRefreshRecyclerviewBinding
import com.habitrpg.android.habitica.extensions.subscribeWithErrorHandler
import com.habitrpg.android.habitica.helpers.MainNavigationController
import com.habitrpg.android.habitica.helpers.RxErrorHandler
import com.habitrpg.android.habitica.models.inventory.*
import com.habitrpg.android.habitica.models.user.OwnedPet
import com.habitrpg.android.habitica.models.user.User
import com.habitrpg.android.habitica.ui.activities.MainActivity
import com.habitrpg.android.habitica.ui.adapter.inventory.ItemRecyclerAdapter
import com.habitrpg.android.habitica.ui.fragments.BaseFragment
import com.habitrpg.android.habitica.ui.helpers.*
import com.habitrpg.android.habitica.ui.views.HabiticaSnackbar
import com.habitrpg.android.habitica.ui.views.HabiticaSnackbar.Companion.showSnackbar
import io.reactivex.functions.Consumer
import javax.inject.Inject

class ItemRecyclerFragment : BaseFragment<FragmentItemsBinding>(), SwipeRefreshLayout.OnRefreshListener {

    @Inject
    lateinit var inventoryRepository: InventoryRepository
    @Inject
    lateinit var userRepository: UserRepository
    var adapter: ItemRecyclerAdapter? = null
    var itemType: String? = null
    var itemTypeText: String? = null
    var isHatching: Boolean = false
    var isFeeding: Boolean = false
    private var hatchingItem: Item? = null
    var feedingPet: Pet? = null
    var user: User? = null
    internal var layoutManager: androidx.recyclerview.widget.LinearLayoutManager? = null

    override var binding: FragmentItemsBinding? = null

    override fun createBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentItemsBinding {
        return FragmentItemsBinding.inflate(inflater, container, false)
    }

    override fun onDestroy() {
        inventoryRepository.close()
        super.onDestroy()
    }

    override fun injectFragment(component: UserComponent) {
        component.inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.recyclerView?.setEmptyView(binding?.emptyView)
        binding?.refreshLayout?.setOnRefreshListener(this)
        binding?.emptyTextView?.text = getString(R.string.empty_items, itemTypeText)

        val context = activity

        layoutManager = androidx.recyclerview.widget.LinearLayoutManager(context)
        binding?.recyclerView?.layoutManager = layoutManager

        adapter = binding?.recyclerView?.adapter as? ItemRecyclerAdapter
        if (adapter == null) {
            context?.let {
                adapter = ItemRecyclerAdapter(null, true, context)
                adapter?.isHatching = this.isHatching
                adapter?.isFeeding = this.isFeeding
                adapter?.fragment = this
            }
            if (this.hatchingItem != null) {
                adapter?.hatchingItem = this.hatchingItem
            }
            if (this.feedingPet != null) {
                adapter?.feedingPet = this.feedingPet
            }
            binding?.recyclerView?.adapter = adapter

            adapter?.let { adapter ->
                compositeSubscription.add(adapter.getSellItemFlowable()
                        .flatMap { item -> inventoryRepository.sellItem(user, item) }
                        .subscribe({ }, RxErrorHandler.handleEmptyError()))

                compositeSubscription.add(adapter.getQuestInvitationFlowable()
                        .flatMap { quest -> inventoryRepository.inviteToQuest(quest) }
                        .subscribe({ MainNavigationController.navigate(R.id.partyFragment) }, RxErrorHandler.handleEmptyError()))
                compositeSubscription.add(adapter.getOpenMysteryItemFlowable()
                        .flatMap { inventoryRepository.openMysteryItem(user) }
                        .doOnNext {
                            val activity = activity as? MainActivity
                            if (activity != null) {
                                DataBindingUtils.loadImage("shop_${it.key}") {image ->
                                    showSnackbar(activity.snackbarContainer, BitmapDrawable(context?.resources, image), null, getString(R.string.mystery_item_received, it.text), HabiticaSnackbar.SnackbarDisplayType.NORMAL)
                                }
                            }
                        }
                        .flatMap { userRepository.retrieveUser(false) }
                        .subscribe({ }, RxErrorHandler.handleEmptyError()))
                compositeSubscription.add(adapter.startHatchingEvents.subscribeWithErrorHandler { showHatchingDialog(it) })
                compositeSubscription.add(adapter.hatchPetEvents.subscribeWithErrorHandler { hatchPet(it.first, it.second) })
            }
        }
        activity?.let {
            binding?.recyclerView?.addItemDecoration(androidx.recyclerview.widget.DividerItemDecoration(it, androidx.recyclerview.widget.DividerItemDecoration.VERTICAL))
        }
        binding?.recyclerView?.itemAnimator = SafeDefaultItemAnimator()

        if (savedInstanceState != null) {
            this.itemType = savedInstanceState.getString(ITEM_TYPE_KEY, "")
        }

        when {
            this.isHatching -> {
                dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
                binding?.titleTextView?.text = getString(R.string.hatch_with, this.hatchingItem?.text)
                binding?.titleTextView?.visibility = View.VISIBLE
                binding?.footerTextView?.text = getString(R.string.hatching_market_info)
                binding?.footerTextView?.visibility = View.VISIBLE
                binding?.openMarketButton?.visibility = View.VISIBLE
            }
            this.isFeeding -> {
                dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
                binding?.titleTextView?.text = getString(R.string.dialog_feeding, this.feedingPet?.text)
                binding?.titleTextView?.visibility = View.VISIBLE
                binding?.footerTextView?.text = getString(R.string.feeding_market_info)
                binding?.footerTextView?.visibility = View.VISIBLE
                binding?.openMarketButton?.visibility = View.VISIBLE
            }
            else -> {
                binding?.titleTextView?.visibility = View.GONE
                binding?.footerTextView?.visibility = View.GONE
                binding?.openMarketButton?.visibility = View.GONE
            }
        }

        binding?.openMarketButton?.setOnClickListener {
            dismiss()
            openMarket()
        }

        binding?.openEmptyMarketButton?.setOnClickListener { openMarket() }

        this.loadItems()
    }

    private fun showHatchingDialog(item: Item) {
        val fragment = ItemRecyclerFragment()
        if (item is Egg) {
            fragment.itemType = "hatchingPotions"
            fragment.hatchingItem = item
        } else {
            fragment.itemType = "eggs"
            fragment.hatchingItem = item
        }
        fragment.isHatching = true
        fragment.isFeeding = false
        parentFragmentManager.let { fragment.show(it, "hatchingDialog") }
    }

    override fun onResume() {
        if ((this.isHatching || this.isFeeding) && dialog?.window != null) {
            val params = dialog?.window?.attributes
            params?.width = ViewGroup.LayoutParams.MATCH_PARENT
            params?.verticalMargin = 60f
            dialog?.window?.attributes = params
        }

        super.onResume()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(ITEM_TYPE_KEY, this.itemType)
    }

    override fun onRefresh() {
        binding?.refreshLayout?.isRefreshing = true
        compositeSubscription.add(userRepository.retrieveUser(true, true)
                .doOnTerminate {
                    binding?.refreshLayout?.isRefreshing = false
                }.subscribe({ }, RxErrorHandler.handleEmptyError()))
    }

    private fun hatchPet(potion: HatchingPotion, egg: Egg) {
        dismiss()
        (activity as? MainActivity)?.hatchPet(potion, egg)
    }

    private fun loadItems() {
        val itemClass: Class<out Item> = when (itemType) {
            "eggs" -> Egg::class.java
            "hatchingPotions" -> HatchingPotion::class.java
            "food" -> Food::class.java
            "quests" -> QuestContent::class.java
            "special" -> SpecialItem::class.java
            else -> Egg::class.java
        }
        itemType?.let { type ->
            compositeSubscription.add(inventoryRepository.getOwnedItems(type)
                    .doOnNext { items ->
                        if (items.size > 0) {
                            val filteredItems = if (isFeeding) {
                                items.where().notEqualTo("key", "Saddle").findAll()
                            } else {
                                items
                            }
                            adapter?.updateData(filteredItems)
                        }
                    }
                    .map { items -> items.mapNotNull { it.key } }
                    .flatMap { inventoryRepository.getItems(itemClass, it.toTypedArray()) }
                    .map {
                        val itemMap = mutableMapOf<String, Item>()
                        for (item in it) {
                            itemMap[item.key] = item
                        }
                        itemMap
                    }
                    .subscribe({ items ->
                    adapter?.items = items
            }, RxErrorHandler.handleEmptyError()))
        }

        compositeSubscription.add(inventoryRepository.getPets().subscribe({ adapter?.setExistingPets(it) }, RxErrorHandler.handleEmptyError()))
        compositeSubscription.add(inventoryRepository.getOwnedPets().firstElement()
                .map { ownedMounts ->
                    val mountMap = mutableMapOf<String, OwnedPet>()
                    ownedMounts.forEach { mountMap[it.key ?: ""] = it }
                    return@map mountMap
                }
                .subscribe({ adapter?.setOwnedPets(it) }, RxErrorHandler.handleEmptyError()))
    }

    private fun openMarket() {
        MainNavigationController.navigate(R.id.shopsFragment)
    }

    companion object {

        private const val ITEM_TYPE_KEY = "CLASS_TYPE_KEY"
    }
}
