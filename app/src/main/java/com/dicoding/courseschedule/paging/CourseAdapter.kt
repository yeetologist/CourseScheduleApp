package com.dicoding.courseschedule.paging

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import com.dicoding.courseschedule.R
import com.dicoding.courseschedule.data.Course

//TODO 6 : Implement Method for PagedListAdapter
class CourseAdapter(private val clickListener: (Course) -> Unit) :
    PagedListAdapter<Course, CourseViewHolder>(DIFF_CALLBACK) {

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Course>() {
            override fun areItemsTheSame(oldItem: Course, newItem: Course): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Course, newItem: Course): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CourseViewHolder {
        val binding =
            LayoutInflater.from(parent.context).inflate(R.layout.item_course, parent, false)
        return CourseViewHolder(binding)
//        throw NotImplementedError()
    }

    override fun onBindViewHolder(holder: CourseViewHolder, position: Int) {
        val courseItem =
            getItem(position) // Assuming you have a getItem(position) method in your PagedListAdapter

        courseItem?.let {
            holder.bind(it, clickListener)
        }
    }
}