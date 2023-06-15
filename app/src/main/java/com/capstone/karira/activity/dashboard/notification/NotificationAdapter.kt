import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.capstone.karira.databinding.ItemNotificationBinding
import com.capstone.karira.model.Notification
import java.text.SimpleDateFormat
import java.util.Locale

class NotificationAdapter(private val notifications: ArrayList<Notification>) : RecyclerView.Adapter<NotificationAdapter.ViewHolder>() {

    fun updateNotifications(newNotifications: ArrayList<Notification>) {
        notifications.clear()
        notifications.addAll(newNotifications)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemNotificationBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        if (notifications.isNotEmpty()) {
            val notification = notifications[position]
            holder.bind(notification)
        }
    }

    override fun getItemCount(): Int {
        return notifications.size
    }

    inner class ViewHolder(private val binding: ItemNotificationBinding) : RecyclerView.ViewHolder(binding.root) {
        private val titleTextView = binding.tvTitle
        private val descriptionTextView = binding.tvDescription
        private val createdAtTextView = binding.tvCreatedAt

        fun bind(notification: Notification) {
            titleTextView.text = notification.title
            descriptionTextView.text = notification.description
            val dateFormatted = notification.createdAt?.let { createdAt ->
                val locale = Locale("id", "ID")
                val dateFormat = SimpleDateFormat("dd MMMM yyyy", locale)
                dateFormat.format(createdAt)
            }
            createdAtTextView.text = dateFormatted
        }
    }
}
