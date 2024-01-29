package co.`in`.plantonic.Adapter

import androidx.recyclerview.widget.RecyclerView
import android.view.ViewGroup
import android.view.LayoutInflater
import co.`in`.plantonic.R
import android.content.Context
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import co.`in`.plantonic.retrofit.models.track.ScanDetail

class TrackStatusAdapter(
    private val context: Context,
    private val scanDetailList: List<ScanDetail>
) : RecyclerView.Adapter<TrackStatusAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(context).inflate(R.layout.item_shipment_details, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val scanDetail = scanDetailList[position]
        holder.trackDate.text = scanDetail.ScanDate
        holder.trackTitle.text = scanDetail.Scan
        holder.trackLocation.text = scanDetail.ScannedLocation
        holder.trackTime.text = scanDetail.ScanTime
        if (position == scanDetailList.size - 1){
            holder.trackLowerLine.visibility = View.GONE
        }else{
            holder.trackLowerLine.visibility = View.VISIBLE
        }
    }

    override fun getItemCount(): Int {
        return scanDetailList.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var trackTitle: TextView
        var trackDate: TextView
        var trackTime: TextView
        var trackLocation: TextView
        var trackLowerLine: ImageView

        init {
            trackTitle = itemView.findViewById<TextView>(R.id.trackTitle)
            trackDate = itemView.findViewById<TextView>(R.id.trackDate)
            trackTime = itemView.findViewById<TextView>(R.id.trackTime)
            trackLocation = itemView.findViewById<TextView>(R.id.trackLocation)
            trackLowerLine = itemView.findViewById<ImageView>(R.id.trackLowerLine)
        }
    }
}