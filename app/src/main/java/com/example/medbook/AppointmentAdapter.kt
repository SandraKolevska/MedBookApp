package com.example.medbook

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class AppointmentAdapter(
    private val appointmentList: List<Appointment>
) : RecyclerView.Adapter<AppointmentAdapter.AppointmentViewHolder>() {

    class AppointmentViewHolder(itemView: View)
        : RecyclerView.ViewHolder(itemView) {

        val doctorName: TextView =
            itemView.findViewById(R.id.appointmentDoctorName)

        val specialization: TextView =
            itemView.findViewById(R.id.appointmentSpecialization)

        val date: TextView =
            itemView.findViewById(R.id.appointmentDate)

        val slot: TextView =
            itemView.findViewById(R.id.appointmentSlot)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AppointmentViewHolder {

        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.appointment_item, parent, false)

        return AppointmentViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: AppointmentViewHolder,
        position: Int
    ) {

        val appointment = appointmentList[position]

        holder.doctorName.text =
            appointment.doctorName

        holder.specialization.text =
            appointment.specialization

        holder.date.text =
            appointment.date

        holder.slot.text =
            appointment.slot
    }

    override fun getItemCount(): Int {
        return appointmentList.size
    }
}