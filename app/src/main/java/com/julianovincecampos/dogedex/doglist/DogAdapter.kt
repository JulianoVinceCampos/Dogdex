package com.julianovincecampos.dogedex.doglist

import com.julianovincecampos.dogedex.model.Dog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.julianovincecampos.dogedex.R
import com.julianovincecampos.dogedex.databinding.DogListItemBinding

class DogAdapter : ListAdapter<Dog, DogAdapter.DogViewHolder>(DiffCallback) {

    companion object DiffCallback : DiffUtil.ItemCallback<Dog>() {

        override fun areItemsTheSame(oldItem: Dog, newItem: Dog): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: Dog, newItem: Dog): Boolean {
            return oldItem.id == newItem.id
        }
    }

    private var onIntemListener: ((Dog) -> Unit)? = null
    fun setOnItemClickListener(onIntemListener: (Dog) -> Unit) {
        this.onIntemListener = onIntemListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DogViewHolder {
        val binding = DogListItemBinding.inflate(LayoutInflater.from(parent.context))
        return DogViewHolder(binding)
    }

    override fun onBindViewHolder(dogViewHolder: DogViewHolder, position: Int) {
        val dog = getItem(position)
        dogViewHolder.bind(dog)
    }

    inner class DogViewHolder(private val binding: DogListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(dog: Dog) {
            if (dog.inCollection) {
                binding.dogListItemLayout.background = ContextCompat.getDrawable(
                    binding.dogImage.context, R.drawable.dog_list_item_background
                )
                binding.dogIndex.visibility = View.GONE
                binding.dogImage.visibility = View.VISIBLE

                binding.dogImage.load(dog.imageUrl)
            } else {
                binding.dogIndex.visibility = View.VISIBLE
                binding.dogImage.visibility = View.GONE
                binding.dogIndex.text = dog.index.toString()
                binding.dogListItemLayout.background = ContextCompat.getDrawable(
                    binding.dogImage.context, R.drawable.dog_list_item_null_backgroud
                )
//                binding.dogListItemLayout.setOnLongClickListener {
//                    onLongItemListener?.invoke(dog)
//                    true
//                }
            }
        }
    }
}