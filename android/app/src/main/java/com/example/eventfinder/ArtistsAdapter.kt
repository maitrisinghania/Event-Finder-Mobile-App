import android.content.Context
import android.content.Intent
import android.net.Uri
import android.text.SpannableString
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.text.method.ScrollingMovementMethod
import android.text.style.ClickableSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.eventfinder.R
import com.example.eventfinder.fragments.ArtistFragment
import com.google.android.material.progressindicator.CircularProgressIndicator

class ArtistsAdapter(private val mList: ArrayList<ArtistFragment.ArtistsList>, private var context: Context) : RecyclerView.Adapter<ArtistsAdapter.ViewHolder>() {

    // create new views
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recycler_artists, parent, false)

        return ViewHolder(view)
    }

    // binds the list items to a view
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val artistList = mList[position]
        holder.name.isSelected = true
        holder.name.movementMethod = ScrollingMovementMethod()
        holder.name.text = artistList.name
        holder.popularityText.text = artistList.popularity.toString()
        holder.followers.text = artistList.followers
        holder.progress.progress = artistList.popularity
        Glide.with(context)
            .load(artistList.img)
            .into(holder.artistImg)

        var imgAlbum1 = artistList.albums[0]
        var imgAlbum2 = artistList.albums[1]
        var imgAlbum3 = artistList.albums[2]
        var link = artistList.spotify
        Glide.with(context).load(imgAlbum1).into(holder.album1)
        Glide.with(context).load(imgAlbum2).into(holder.album2)
        Glide.with(context).load(imgAlbum3).into(holder.album3)

        val linkText = "Check out on Spotify"
        val spannable = SpannableString(linkText)
        val clickableSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {

                val url = link
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                widget.context.startActivity(intent)

            }
        }
        spannable.setSpan(clickableSpan, 0, spannable.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

        // Set text to TextView
        holder.spotify.text = spannable

        holder.spotify.movementMethod = LinkMovementMethod.getInstance()







    }

    // return the number of the items in the list
    override fun getItemCount(): Int {
        return mList.size
    }




    // Holds the views for adding it to image and text
    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {

        var name: TextView = itemView.findViewById<TextView>(R.id.artistName)
        var followers = itemView.findViewById<TextView>(R.id.followers)
        var spotify = itemView.findViewById<TextView>(R.id.spotify)
        var artistImg = itemView.findViewById<ImageView>(R.id.artist_img)
        var popularityText = itemView.findViewById<TextView>(R.id.popularityText)
        var progress = itemView.findViewById<CircularProgressIndicator>(R.id.progressIndicator)
        var album1 = itemView.findViewById<ImageView>(R.id.album1)
        var album2 = itemView.findViewById<ImageView>(R.id.album2)
        var album3 = itemView.findViewById<ImageView>(R.id.album3)



    }
}
