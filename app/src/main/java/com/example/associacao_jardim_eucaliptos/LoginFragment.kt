package com.example.associacao_jardim_eucaliptos

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.SpannableString
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
private lateinit var mainActivity: MainActivity
private lateinit var email: EditText
private lateinit var password: EditText
private lateinit var loginButton: Button

/**
 * A simple [Fragment] subclass.
 * Use the [LoginFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class LoginFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
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


        val signupPrefix = getString(R.string.signup_text_prefix)
        val signupLink = getString(R.string.signup_link)

        val fullText = "$signupPrefix $signupLink"

        val spannableString = SpannableString(fullText)

        loginButton.setOnClickListener { view ->
            if (!validateEmail() || !validatePassword()) {
                // Handle invalid email or password
            } else {
                checkUser()
            }
        }

        val clickableSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {
                // Navega para o fragment_events
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

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            LoginFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
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
        if (value.isEmpty()) {
            email.error = "O campo e-mail não pode estar em branco!"
            return false
        } else {
            email.error = null
            return true
        }
    }

    fun validatePassword(): Boolean {
        val value = password.text.toString()
        if (value.isEmpty()) {
            password.error = "O campo senha não pode estar em branco!"
            return false
        } else {
            password.error = null
            return true
        }
    }

    fun checkUser() {
        val emailStr = email.text.toString().trim()
        val passwordStr = password.text.toString().trim()

        val reference = FirebaseDatabase.getInstance().getReference("users")
        val checkUserDatabase = reference.orderByChild("email").equalTo(emailStr)

        checkUserDatabase.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (userSnapshot in snapshot.children) {
                        val userId = userSnapshot.key // Retrieve the user's unique identifier
                        val userRef = reference.child(userId!!) // Construct the database reference for the user
                        // Now, query the user's password
                        userRef.addListenerForSingleValueEvent(object : ValueEventListener {
                            override fun onDataChange(userSnapshot: DataSnapshot) {
                                val passwordFromDB = userSnapshot.child("password").getValue(String::class.java)

                                if (passwordFromDB != null && passwordFromDB == passwordStr) {
                                    val fragment = HomeFragment()
                                    val transaction = requireActivity().supportFragmentManager.beginTransaction()
                                    transaction.replace(R.id.fragment_container, fragment)
                                    transaction.addToBackStack(null)
                                    transaction.commit()

                                    val mainActivity = requireActivity() as? MainActivity
                                    mainActivity?.showToolbarAndBottomNavigation()

                                } else {
                                    // Password does not match, show error
                                    password.error = "Credenciais inválidas!"
                                    password.requestFocus()
                                }
                            }

                            override fun onCancelled(error: DatabaseError) {
                            }
                        })
                    }
                } else {
                    // User does not exist, show error
                    email.error = "Esse usuário não existe!"
                    email.requestFocus()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle onCancelled
            }
        })
    }


}





