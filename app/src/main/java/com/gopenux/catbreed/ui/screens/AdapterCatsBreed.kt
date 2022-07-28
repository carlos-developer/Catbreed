package com.gopenux.catbreed.ui.screens

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
import com.gopenux.catbreed.R
import com.gopenux.catbreed.domain.models.Cat

class AdapterCatsBreed(
    private val activity: NavController,
    private var imagesCat: List<Cat>,
    private val resultListener: ResultListener,
) :
    RecyclerView.Adapter<AdapterCatsBreed.Holder>(), Filterable {

    private lateinit var context: Context
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        context = parent.context
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.adapter_card_cat_breed, parent, false)
        return Holder(view, activity)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val cat = imagesCat[position]
        holder.textViewName.text = cat.name
        holder.textViewPaisOrigen.text = cat.origin
        holder.selectCard(holder.textViewMas, cat)
        Glide.with(context).load(cat.urlImage).centerCrop().into(holder.imageView)
    }

    override fun getItemCount() = imagesCat.size

    class Holder(view: View, private val activity: NavController) : RecyclerView.ViewHolder(view) {
        var textViewName: TextView = view.findViewById(R.id.textViewNameRaza)
        val textViewPaisOrigen: TextView = view.findViewById(R.id.textViewPaisOrigen)
        val textViewInteligencia: TextView = view.findViewById(R.id.textViewInteligencia)
        val textViewMas: TextView = view.findViewById(R.id.textViewMas)
        val imageView: ImageView = view.findViewById(R.id.imageViewCat)

        fun selectCard(textView: TextView, cat: Cat) {
            textView.setOnClickListener {
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
                    val listResult = mutableListOf<Cat>()
                    val search = constraint.toString()
                    for (imageCat in imagesCat) {
                        if (imageCat.name.equals(search, true)) {
                            listResult.add(imageCat)
                        }
                    }
                    if (listResult.isEmpty()) {
                        resultListener.emptyResult()
                    } else {
                        filterResults.count = listResult.size
                        filterResults.values = listResult
                    }
                }
                return filterResults
            }

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