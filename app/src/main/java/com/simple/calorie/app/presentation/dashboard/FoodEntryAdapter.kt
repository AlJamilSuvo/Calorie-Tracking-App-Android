package com.simple.calorie.app.presentation.dashboard

import android.content.DialogInterface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.simple.calorie.app.databinding.FoodEntrySingleItemLayoutBinding
import java.text.SimpleDateFormat

class FoodEntryAdapter(private val foodEntryContextMenuListener: FoodEntryContextMenuListener) :
    RecyclerView.Adapter<FoodEntryVideHolder>() {

    private val diffCallback: DiffUtil.ItemCallback<FoodEntryItemModel> =
        object : DiffUtil.ItemCallback<FoodEntryItemModel>() {
            override fun areItemsTheSame(
                oldItem: FoodEntryItemModel,
                newItem: FoodEntryItemModel
            ): Boolean {
                return oldItem.foodEntry.entryId == newItem.foodEntry.entryId
            }

            override fun areContentsTheSame(
                oldItem: FoodEntryItemModel,
                newItem: FoodEntryItemModel
            ): Boolean {
                return oldItem.foodEntry.entryId == newItem.foodEntry.entryId && oldItem.foodEntry.calorie == newItem.foodEntry.calorie &&
                        oldItem.foodEntry.timestamp == newItem.foodEntry.timestamp && oldItem.isHeader == newItem.isHeader
                        && oldItem.totalCalorie == newItem.totalCalorie && oldItem.totalCalorieStatus == newItem.totalCalorieStatus

            }

        }

    private val asyncListDiffer: AsyncListDiffer<FoodEntryItemModel> =
        AsyncListDiffer(this, diffCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodEntryVideHolder {
        val binding = FoodEntrySingleItemLayoutBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return FoodEntryVideHolder(binding)
    }

    override fun onBindViewHolder(holder: FoodEntryVideHolder, position: Int) {
        val item = asyncListDiffer.currentList[position]
        val foodEntry = item.foodEntry
        holder.binding.tvFoodName.text = foodEntry.foodName
        holder.binding.tvAmount.text = "${foodEntry.calorie} Cal"
        val simpleDateFormat = SimpleDateFormat("HH:mm dd/MM/yyyy")
        holder.binding.tvDate.text = simpleDateFormat.format(foodEntry.timestamp)
        holder.binding.tvUserName.text = foodEntry.userId

        if (item.isHeader) {
            holder.binding.tvHeader.visibility = View.VISIBLE
            holder.binding.tvTotalCalorie.visibility = View.VISIBLE
            holder.binding.tvHeader.text = item.dateStr
            if (item.totalCalorie > 0f)
                holder.binding.tvTotalCalorie.text =
                    "${item.totalCalorie} Cal (${item.totalCalorieStatus})"
        } else {
            holder.binding.tvHeader.visibility = View.GONE
            holder.binding.tvTotalCalorie.visibility = View.GONE
        }

        holder.binding.root.setOnLongClickListener {
            val dialogBuilder = AlertDialog.Builder(holder.binding.root.context)
            dialogBuilder.setMessage("Modify/Delete  ${foodEntry.foodName}")
            dialogBuilder.setPositiveButton("Modify") { dialogInterface: DialogInterface, i: Int ->
                foodEntryContextMenuListener.editItem(foodEntry)
                dialogInterface.dismiss()
            }

            dialogBuilder.setNegativeButton("Delete") { dialogInterface: DialogInterface, i: Int ->
                foodEntryContextMenuListener.deleteItem(foodEntry)
                dialogInterface.dismiss()
            }

            dialogBuilder.show()


            return@setOnLongClickListener true
        }
    }

    fun submitList(list: List<FoodEntryItemModel>) {
        asyncListDiffer.submitList(list)
    }

    override fun getItemCount(): Int =
        asyncListDiffer.currentList.size
}

class FoodEntryVideHolder(val binding: FoodEntrySingleItemLayoutBinding) :
    RecyclerView.ViewHolder(binding.root)