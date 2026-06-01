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

    private fun translateSpecialization(
        specialization: String,
        isMk: Boolean
    ): String {

        if (!isMk) return specialization

        return when (specialization) {

            "Cardiologist" -> "Кардиолог"
            "Dentist" -> "Стоматолог"
            "Pediatrician" -> "Педијатар"
            "Neurologist" -> "Невролог"
            "Dermatologist" -> "Дерматолог"
            "Orthopedic" -> "Ортопед"
            "Gynecologist" -> "Гинеколог"
            "Psychiatrist" -> "Психијатар"
            "Ophthalmologist" -> "Офталмолог"
            "ENT Specialist" -> "ОРЛ Специјалист"
            "Radiologist" -> "Радиолог"
            "General Surgeon" -> "Општ Хирург"

            else -> specialization
        }
    }

    private fun translateExperience(
        experience: String,
        isMk: Boolean
    ): String {

        if (!isMk) return experience

        return experience
            .replace(
                " years experience",
                " години искуство"
            )
            .replace(
                " year experience",
                " година искуство"
            )
    }

    private fun translateDescription(
        description: String,
        isMk: Boolean
    ): String {

        if (!isMk) return description

        return when (description) {

            "Experienced cardiologist specialized in heart disease prevention, ECG diagnostics and patient care." ->
                "Искусен кардиолог специјализиран за превенција на срцеви заболувања, ЕКГ дијагностика и грижа за пациентите."

            "Professional dentist with extensive experience in cosmetic dentistry and oral health treatments." ->
                "Професионален стоматолог со долгогодишно искуство во естетска стоматологија и третмани за орално здравје."

            "Friendly pediatrician dedicated to children's health, preventive care and family consultations." ->
                "Пријателски настроен педијатар посветен на здравјето на децата, превентивната грижа и семејните консултации."

            "Highly experienced neurologist focused on brain disorders, migraines and neurological diagnostics." ->
                "Високо искусен невролог специјализиран за мозочни заболувања, мигрени и невролошка дијагностика."

            "Dermatology specialist experienced in skin care treatments, acne therapy and cosmetic procedures." ->
                "Дерматолог специјализиран за третмани на кожа, терапија за акни и естетски процедури."

            "Orthopedic specialist helping patients recover from injuries, fractures and joint problems." ->
                "Ортопед кој им помага на пациентите во закрепнување од повреди, скршеници и проблеми со зглобовите."

            "Experienced gynecologist focused on women's health, pregnancy care and preventive examinations." ->
                "Искусен гинеколог специјализиран за здравјето на жените, следење на бременост и превентивни прегледи."

            "Compassionate psychiatrist helping patients with anxiety, stress management and mental wellness." ->
                "Психијатар кој им помага на пациентите во справување со анксиозност, стрес и ментална благосостојба."

            "Eye specialist providing vision care, diagnostics and treatment for various eye conditions." ->
                "Офталмолог кој обезбедува грижа за видот, дијагностика и третман на различни очни заболувања."

            "ENT specialist treating ear, nose and throat conditions with modern medical approaches." ->
                "ОРЛ специјалист за лекување на заболувања на уво, нос и грло со современи медицински пристапи."

            "Expert radiologist specialized in MRI, CT scan diagnostics and medical imaging interpretation." ->
                "Радиолог специјализиран за МРИ, КТ дијагностика и интерпретација на медицински снимки."

            "Highly skilled surgeon with extensive experience in complex surgeries and patient recovery care." ->
                "Високо квалификуван хирург со долгогодишно искуство во сложени операции и постоперативна грижа за пациентите."

            else -> description
        }
    }

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

        val isMk =
            holder.itemView.context.resources
                .configuration
                .locales[0]
                .language == "mk"

        holder.doctorImage.setImageResource(
            doctor.imageResId
        )

        holder.doctorName.text =
            doctor.name

        holder.doctorSpecialization.text =
            translateSpecialization(
                doctor.specialization,
                isMk
            )

        holder.doctorRating.text =
            doctor.rating

        holder.doctorExperience.text =
            translateExperience(
                doctor.experience,
                isMk
            )

        holder.doctorPrice.text =
            doctor.price

        holder.doctorExperience.setOnClickListener {

            AlertDialog.Builder(
                holder.itemView.context
            )
                .setTitle(doctor.name)

                .setMessage(
                    "${translateDescription(doctor.description, isMk)}\n\n" +
                            "${holder.itemView.context.getString(R.string.consultation_fee)}: ${doctor.price}"
                )

                .setPositiveButton(
                    holder.itemView.context.getString(R.string.ok),
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