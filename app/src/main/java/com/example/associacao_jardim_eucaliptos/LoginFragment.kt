package com.example.associacao_jardim_eucaliptos

import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.SpannableString
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
private lateinit var mainActivity: MainActivity
private lateinit var email: EditText
private lateinit var password: EditText
private lateinit var loginButton: Button
private lateinit var forgotPasswordButton: Button
private lateinit var auth: FirebaseAuth

class LoginFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null

    override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        if (currentUser != null) {
            val intent = Intent(requireContext(), MainActivity::class.java)
            startActivity(intent)
            requireActivity().finish()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
        auth = FirebaseAuth.getInstance()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_login, container, false)

        val textView = view.findViewById<TextView>(R.id.signupText)
        email = view.findViewById(R.id.email)
        password = view.findViewById(R.id.password)
        loginButton = view.findViewById(R.id.loginButton)
        forgotPasswordButton = view.findViewById(R.id.forgotPasswordButton)

        val signupPrefix = getString(R.string.signup_text_prefix)
        val signupLink = getString(R.string.signup_link)
        val fullText = "$signupPrefix $signupLink"
        val spannableString = SpannableString(fullText)

        loginButton.setOnClickListener {

            if (!validateEmail() || !validatePassword()) {

            } else {
                ProgressUtils.showProgressDialog(requireContext())
                auth.signInWithEmailAndPassword(email.text.toString(), password.text.toString())
                    .addOnCompleteListener(requireActivity()) { task ->
                        ProgressUtils.hideProgressDialog()
                        if (task.isSuccessful) {
                            Log.d(TAG, "signInWithEmailAndPassword: success")
                            val currentUser = auth.currentUser
                            if (currentUser != null) {
                                checkIfUserIsAdmin(currentUser.uid) { isAdmin ->
                                    mainActivity.updateNavigationMenu(true, isAdmin)
                                    navigateToHomeFragment() // Go back to MainActivity
                                }
                            } else {
                                Log.e(TAG, "Current user is null after successful authentication")
                                Toast.makeText(requireContext(), "Erro ao obter usuário atual", Toast.LENGTH_SHORT).show()
                            }
                        } else {
                            Log.e(TAG, "signInWithEmailAndPassword: failure", task.exception)
                            Toast.makeText(requireContext(), "Falha ao autenticar", Toast.LENGTH_SHORT).show()
                        }
                    }

            }

        }

        forgotPasswordButton.setOnClickListener {
            val emailAddress = email.text.toString().trim()
            if (emailAddress.isEmpty()) {
                email.error = "Por favor, insira seu e-mail!"
                email.requestFocus()
            } else {
                auth.sendPasswordResetEmail(emailAddress)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(requireContext(), "Email de recuperação de senha enviado.", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(requireContext(), "Erro ao enviar e-mail de recuperação.", Toast.LENGTH_SHORT).show()
                        }
                    }
            }
        }

        val clickableSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {
                val fragment = SignUpFragment()
                val transaction = requireActivity().supportFragmentManager.beginTransaction()
                transaction.replace(R.id.fragment_container, fragment)
                transaction.addToBackStack(null)
                transaction.commit()
            }
        }

        val startIndex = fullText.indexOf(signupLink)
        val endIndex = startIndex + signupLink.length
        spannableString.setSpan(clickableSpan, startIndex, endIndex, 0)

        textView.text = spannableString
        textView.movementMethod = LinkMovementMethod.getInstance()

        return view
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is MainActivity) {
            mainActivity = context
            mainActivity.hideToolbarAndBottomNavigation()
        }
    }

    override fun onDetach() {
        super.onDetach()
        mainActivity.showToolbarAndBottomNavigation()
    }

    fun validateEmail(): Boolean {
        val value = email.text.toString()
        return if (value.isEmpty()) {
            email.error = "O campo e-mail não pode estar em branco!"
            false
        } else {
            email.error = null
            true
        }
    }

    fun validatePassword(): Boolean {
        val value = password.text.toString()
        return if (value.isEmpty()) {
            password.error = "O campo senha não pode estar em branco!"
            false
        } else {
            password.error = null
            true
        }
    }

    private fun checkIfUserIsAdmin(uid: String, callback: (Boolean) -> Unit) {
        val userRef = FirebaseDatabase.getInstance().getReference("users").child(uid)
        userRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val userData = snapshot.getValue(HelperClass::class.java)
                callback(userData?.admin ?: false)
            }

            override fun onCancelled(error: DatabaseError) {
                callback(false)
            }
        })
    }

    private fun navigateToHomeFragment() {
        val transaction = requireActivity().supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container, HomeFragment())
        transaction.addToBackStack(null)
        transaction.commit()
        mainActivity.showToolbarAndBottomNavigation()
    }
}
