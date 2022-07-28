package com.catbreed.ui.screens

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.catbreed.domain.models.Cat
import com.catbree.R

class AdapterCatsBreed(
    private val activity: NavController,
    private var imagesCat: List<Cat>,
    private val resultListener: ResultListener,
) : RecyclerView.Adapter<AdapterCatsBreed.Holder>(), Filterable {

    private lateinit var context: Context
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        context = parent.context
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.adapter_card_cat_breed, parent, false)
        return Holder(view, activity)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val cat = imagesCat[position]
        holder.textViewName.text =
            String.format(context.getString(R.string.cat_breed_name), cat.name)
        holder.textViewOriginCountry.text =
            String.format(context.getString(R.string.origin_country), cat.origin)
        holder.textViewIntelligence.text =
            String.format(context.getString(R.string.intelligence), cat.intelligence)
        holder.selectCard(cat)
        Glide.with(context).load(cat.urlImage).fitCenter().into(holder.imageViewCat)
        holder.imageViewCat.setPadding(0, 0, 0, 0)
    }

    override fun getItemCount() = imagesCat.size

    class Holder(view: View, private val activity: NavController) : RecyclerView.ViewHolder(view) {
        var textViewName: TextView = view.findViewById(R.id.text_view_name)
        val textViewOriginCountry: TextView = view.findViewById(R.id.text_view_origin_country)
        val textViewIntelligence: TextView = view.findViewById(R.id.text_view_intelligence)
        private val textViewMore: TextView = view.findViewById(R.id.text_view_more)
        val imageViewCat: ImageView = view.findViewById(R.id.image_view_cat)

        fun selectCard(cat: Cat) {
            textViewMore.setOnClickListener {
                val bundle = bundleOf(
                    "name" to cat.name,
                    "adaptability" to cat.adaptability,
                    "origin" to cat.origin,
                    "intelligence" to cat.intelligence,
                    "description" to cat.description,
                    "lifeSpan" to cat.lifeSpan,
                    "urlImage" to cat.urlImage
                )
                activity.navigate(R.id.action_nav_home_to_fragment_cat_breed_detail, bundle)
            }
        }
    }

    override fun getFilter(): Filter {
        val filter: Filter = object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val filterResults = FilterResults()
                if (constraint == null || constraint.isEmpty()) {
                    filterResults.count = imagesCat.size
                    filterResults.values = imagesCat
                } else {
                    val search = constraint.toString()
                    val listResult = imagesCat.filter { it.name?.contains(search, true) == true }
                    if (listResult.isEmpty()) {
                        resultListener.emptyResult()
                    } else {
                        filterResults.count = listResult.size
                        filterResults.values = listResult
                    }
                }
                return filterResults
            }

            @Suppress("UNCHECKED_CAST")
            override fun publishResults(p0: CharSequence?, result: FilterResults?) {
                if (result?.values != null) {
                    imagesCat = result.values as List<Cat>
                    notifyDataSetChanged()
                }
            }
        }
        return filter
    }
}