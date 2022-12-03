package com.simple.calorie.app.presentation.dashboard

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.simple.calorie.app.databinding.UserAverageSingleItemLayoutBinding

class UserAverageListAdapter() :
    RecyclerView.Adapter<UserAverageViewHolder>() {

    private val diffCallback: DiffUtil.ItemCallback<UserAverageItem> =
        object : DiffUtil.ItemCallback<UserAverageItem>() {
            override fun areItemsTheSame(
                oldItem: UserAverageItem,
                newItem: UserAverageItem
            ): Boolean {
                return oldItem.userId == newItem.userId
            }

            override fun areContentsTheSame(
                oldItem: UserAverageItem,
                newItem: UserAverageItem
            ): Boolean {
                return oldItem.userId == newItem.userId && oldItem.averageCalorie == newItem.averageCalorie

            }

        }

    private val asyncListDiffer: AsyncListDiffer<UserAverageItem> =
        AsyncListDiffer(this, diffCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserAverageViewHolder {
        val binding = UserAverageSingleItemLayoutBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return UserAverageViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UserAverageViewHolder, position: Int) {
        val item = asyncListDiffer.currentList[position]
        holder.binding.tvUserId.text = item.userId
        holder.binding.tvAverageCal.text = "${item.averageCalorie}"

    }

    fun submitList(list: List<UserAverageItem>) {
        asyncListDiffer.submitList(list)
    }

    override fun getItemCount(): Int =
        asyncListDiffer.currentList.size
}

class UserAverageViewHolder(val binding: UserAverageSingleItemLayoutBinding) :
    RecyclerView.ViewHolder(binding.root)