package com.example.wazitoecommerce.data

import android.app.ProgressDialog
import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.navigation.NavHostController
import com.example.wazitoecommerce.models.Doctor
import com.example.wazitoecommerce.models.Product
import com.example.wazitoecommerce.navigation.LOGIN_URL
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage

class DoctorViewModel(var navController:NavHostController, var context: Context) {
    var authViewModel:AuthViewModel
    var progress: ProgressDialog
    init {
        authViewModel = AuthViewModel(navController, context)
        if (!authViewModel.isLoggedIn()){
            navController.navigate(LOGIN_URL)
        }
        progress = ProgressDialog(context)
        progress.setTitle("Loading")
        progress.setMessage("Please wait...")
    }

    fun uploadDoctor(name:String, specialisation:String, age:String, filePath:Uri){
        val doctorId = System.currentTimeMillis().toString()
        val storageRef = FirebaseStorage.getInstance().getReference()
            .child("Doctors/$doctorId")
        progress.show()
        storageRef.putFile(filePath).addOnCompleteListener{
            progress.dismiss()
            if (it.isSuccessful){
                // Save data to db
                storageRef.downloadUrl.addOnSuccessListener {
                    var imageUrl = it.toString()
                    var doctor = Doctor(name,specialisation,age,imageUrl,doctorId)
                    var databaseRef = FirebaseDatabase.getInstance().getReference()
                        .child("Doctors/$doctorId")
                    databaseRef.setValue(doctor).addOnCompleteListener {
                        if (it.isSuccessful){
                            Toast.makeText(this.context, "Success", Toast.LENGTH_SHORT).show()
                        }else{
                            Toast.makeText(this.context, "Error", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }else{
                Toast.makeText(this.context, "Upload error", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun allDoctors(
        doctor: MutableState<Doctor>,
        doctors: SnapshotStateList<Doctor>
    ): SnapshotStateList<Doctor> {
        progress.show()
        var ref = FirebaseDatabase.getInstance().getReference()
            .child("Doctors")
        ref.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                doctors.clear()
                for (snap in snapshot.children){
                    var retrievedDoctor = snap.getValue(Doctor::class.java)
                    doctor.value = retrievedDoctor!!
                    doctors.add(retrievedDoctor)
                }
                progress.dismiss()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, "DB locked", Toast.LENGTH_SHORT).show()
            }
        })
        return doctors
    }

    fun deleteDoctor(doctorId:String){
        var ref = FirebaseDatabase.getInstance().getReference()
            .child("Doctors/$doctorId")
        ref.removeValue()
        Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show()
    }
}