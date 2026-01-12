package com.example.finalprojectpam

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.finalprojectpam.databinding.ActivitySignInBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class SignInActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignInBinding
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)


        firebaseAuth = FirebaseAuth.getInstance()
        binding.textView.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }

        binding.button.setOnClickListener {
            val email = binding.emailEt.text.toString()
            val pass = binding.passET.text.toString()

            if (email.isNotEmpty() && pass.isNotEmpty()) {

                firebaseAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener {
                    if (it.isSuccessful) {

                        val uid = firebaseAuth.currentUser!!.uid
                        val userRef = FirebaseDatabase.getInstance()
                            .getReference("users")
                            .child(uid)

                        userRef.get().addOnSuccessListener { snapshot ->

                            if (!snapshot.exists()) {
                                Toast.makeText(
                                    this,
                                    "Akun tidak memiliki role",
                                    Toast.LENGTH_SHORT
                                ).show()
                                firebaseAuth.signOut()
                                return@addOnSuccessListener
                            }

                            val role = snapshot.child("role").getValue(String::class.java)
                            val divisi = snapshot.child("divisi").getValue(String::class.java)

                            if (role.isNullOrEmpty()) {
                                Toast.makeText(
                                    this,
                                    "Role pengguna tidak ditemukan",
                                    Toast.LENGTH_SHORT
                                ).show()
                                firebaseAuth.signOut()
                                return@addOnSuccessListener
                            }

                            // 👉 SIMPAN ROLE (SESSION)
                            val prefs = getSharedPreferences("USER_SESSION", MODE_PRIVATE)
                            prefs.edit()
                                .putString("ROLE", role)
                                .putString("DIVISI", divisi)
                                .apply()

                            // 👉 MASUK KE LANDING
                            val intent = Intent(this, LandingActivity::class.java)
                            startActivity(intent)
                            finish()

                        }.addOnFailureListener {
                            Toast.makeText(this, "Gagal mengambil data user", Toast.LENGTH_SHORT).show()
                        }
                    }

                }
            } else {
                Toast.makeText(this, "Empty Fields Are not Allowed !!", Toast.LENGTH_SHORT).show()

            }
        }
    }

    override fun onStart() {
        super.onStart()

        val user = FirebaseAuth.getInstance().currentUser
        if (user != null) {
            val prefs = getSharedPreferences("USER_SESSION", MODE_PRIVATE)
            val role = prefs.getString("ROLE", null)

            if (role != null) {
                val intent = Intent(this, LandingActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                finish()
            } else {
                FirebaseAuth.getInstance().signOut()
            }
        }
    }


}