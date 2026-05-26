package com.example.medbook

import android.app.AlertDialog
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class DoctorAdapter(
    private val doctorList: List<Doctor>
) : RecyclerView.Adapter<DoctorAdapter.DoctorViewHolder>() {

    class DoctorViewHolder(itemView: View)
        : RecyclerView.ViewHolder(itemView) {

        val doctorImage: ImageView =
            itemView.findViewById(R.id.doctorImage)

        val doctorName: TextView =
            itemView.findViewById(R.id.doctorName)

        val doctorSpecialization: TextView =
            itemView.findViewById(R.id.doctorSpecialization)

        val doctorRating: TextView =
            itemView.findViewById(R.id.doctorRating)

        val doctorExperience: TextView =
            itemView.findViewById(R.id.doctorExperience)

        val doctorPrice: TextView =
            itemView.findViewById(R.id.doctorPrice)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): DoctorViewHolder {

        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.doctor_item, parent, false)

        return DoctorViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: DoctorViewHolder,
        position: Int
    ) {

        val doctor = doctorList[position]

        holder.doctorImage.setImageResource(
            doctor.imageResId
        )

        holder.doctorName.text =
            doctor.name

        holder.doctorSpecialization.text =
            doctor.specialization

        holder.doctorRating.text =
            doctor.rating

        holder.doctorExperience.text =
            doctor.experience

        holder.doctorPrice.text =
            doctor.price

        holder.doctorExperience.setOnClickListener {

            AlertDialog.Builder(
                holder.itemView.context
            )
                .setTitle(doctor.name)

                .setMessage(
                    "${doctor.description}\n\n" +
                            "Consultation Fee: ${doctor.price}"
                )

                .setPositiveButton(
                    "OK",
                    null
                )

                .show()
        }

        holder.itemView.setOnClickListener {

            val context = holder.itemView.context

            val intent =
                Intent(
                    context,
                    DoctorDetailsActivity::class.java
                )

            intent.putExtra(
                "doctorName",
                doctor.name
            )

            intent.putExtra(
                "doctorSpecialization",
                doctor.specialization
            )

            intent.putExtra(
                "doctorRating",
                doctor.rating
            )

            intent.putExtra(
                "doctorExperience",
                doctor.experience
            )

            intent.putExtra(
                "doctorImage",
                doctor.imageResId
            )

            intent.putExtra(
                "doctorPrice",
                doctor.price
            )

            intent.putExtra(
                "doctorDescription",
                doctor.description
            )

            intent.putStringArrayListExtra(
                "doctorSlots",
                ArrayList(doctor.slots)
            )

            intent.putIntegerArrayListExtra(
                "doctorUnavailableDays",
                ArrayList(doctor.unavailableDays)
            )

            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return doctorList.size
    }
}