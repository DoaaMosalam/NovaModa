package com.holeCode.novamoda.ui.fragments.login


import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.holeCode.novamoda.R
import com.holeCode.novamoda.common.BasicFragment
import com.holeCode.novamoda.data.model.Resource
import com.holeCode.novamoda.databinding.FragmentLoginBinding
import com.holeCode.novamoda.ui.fragments.forget_password.ForgetPasswordFragment
import com.holeCode.novamoda.util.CrashlyticsUtils
import com.holeCode.novamoda.util.LoginException
import com.holeCode.novamoda.util.isEmailValid
import com.holeCode.novamoda.util.isPasswordValid
import com.holeCode.novamoda.util.showSnakeBarError
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class LoginFragment : BasicFragment<FragmentLoginBinding, LoginViewModel>(), TextWatcher {
    override fun getLayoutResID() = R.layout.fragment_login

    private lateinit var checkIcon: Drawable
    override val viewModel: LoginViewModel by viewModels()

    private val callbackManager: CallbackManager by lazy { CallbackManager.Factory.create() }
    private val loginManager: LoginManager by lazy { LoginManager.getInstance() }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        checkIcon = ContextCompat.getDrawable(requireActivity(), R.drawable.baseline_check_24)!!

        binding.loginViewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        addTextWatcher()
        addFocusListener()

        binding.btnLogin.setOnClickListener {
            viewModel.login()
        }

        setUpObserve()
        initListener()
        initViewModel()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DataBindingUtil.inflate(inflater, getLayoutResID(), container, false)

        // Set the Toolbar as the ActionBar
        (requireActivity() as AppCompatActivity).setSupportActionBar(binding.toolbarmain)

        // Set the title for the Toolbar
        (requireActivity() as AppCompatActivity).supportActionBar?.title =
            getString(R.string.login_to_your_account)

        return binding.root
    }

//===============================================================================================
    /*This method call login from LoginViewModel with API*/

    override fun setUpObserve() {
        viewModel.errorMessage.observe(viewLifecycleOwner) { errorMessage ->
            errorMessage?.let {
                Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show()

            }
        }

        viewModel.user.observe(viewLifecycleOwner) { user ->
            user?.let {
                // Handle successful login, e.g., navigate to the next screen
//                progressDialog.show()
                navigateToHome()
                Toast.makeText(requireContext(), "Login successful", Toast.LENGTH_SHORT).show()
            }
        }

    }

    //==============================================================================================
    /*This method call method loginstate from loginViewModel.class with handle login with Google and Facebook*/
    private fun initViewModel() {
        lifecycleScope.launch {
            viewModel.loginState.collect { state ->
                state.let { resource ->
                    when (resource) {
                        is Resource.Loading -> {
                            progressDialog.show()
                        }

                        is Resource.Success -> {
                            progressDialog.dismiss()
                        }

                        is Resource.Error -> {

                            progressDialog.dismiss()
                            val msg =
                                resource.exception?.message ?: getString(R.string.generic_error_msg)
                            Log.d(TAG, "initViewModelError: $msg")
                            view?.showSnakeBarError(msg)
                        }
                    }
                }
            }
        }
    }

    //=====================================================================================
    // handle code login with google.
    private val launcher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == AppCompatActivity.RESULT_OK) {
                val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                handleSignInResult(task)
            } else {
                view?.showSnakeBarError(getString(R.string.google_sign_in_failed_msg))
            }
        }

    private fun loginWithGoogleRequest() {
        val signInIntent = getGoogleRequestIntent(requireActivity())
        launcher.launch(signInIntent)
    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(ApiException::class.java)
            firebaseAuthWithGoogle(account.idToken!!)
        } catch (e: Exception) {
            view?.showSnakeBarError(e.message ?: getString(R.string.generic_error_msg))
            val msg = e.message ?: getString(R.string.generic_error_msg)
            logAuthIssueToCrashlytics(msg, "Google")
        }
    }

    private fun logAuthIssueToCrashlytics(msg: String, provider: String) {
        CrashlyticsUtils.sendCustomLogToCrashlytics<LoginException>(
            msg,
            CrashlyticsUtils.LOGIN_KEY to msg,
            CrashlyticsUtils.LOGIN_PROVIDER to provider,
        )
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        viewModel.loginWithGoogle(idToken)

    }


    //=======================================================================================
    // handle code login with facebook.
    private fun signOut() {
        loginManager.logOut()
        Log.d(TAG, "signOut: ")
    }

    private fun isLoggedIn(): Boolean {
        val accessToken = AccessToken.getCurrentAccessToken()
        return accessToken != null && !accessToken.isExpired
    }

    private fun loginWithFacebook() {
        if (isLoggedIn()) signOut()
        loginManager.registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
            override fun onSuccess(result: LoginResult) {
                val token = result.accessToken.token
                Log.d(TAG, "onSuccess: $token")
                firebaseAuthWithFacebook(token)
            }

            override fun onCancel() {
                // Handle login cancel
            }

            override fun onError(error: FacebookException) {
                // Handle login error
                val msg = error.message ?: getString(R.string.generic_error_msg)
                Log.d(TAG, "onError: $msg")
                view?.showSnakeBarError(msg)
                logAuthIssueToCrashlytics(msg, "Facebook")
            }
        })

        loginManager.logInWithReadPermissions(
            this, callbackManager, listOf("email", "public_profile")
        )
    }

    private fun firebaseAuthWithFacebook(token: String) {
        viewModel.loginWithFacebook(token)
    }

    //============================================================================================
    private fun addTextWatcher() {
        binding.edEmailLogin.addTextChangedListener(this)
        binding.edPasswordLogin.addTextChangedListener(this)
    }

    private fun addFocusListener() {
        emailFocusListener()
        passwordFocusListener()
    }

    private fun validateEmail(): Boolean {
        val email = binding.edEmailLogin.text.toString().trim()
        if (!email.isEmailValid()) {
            binding.emailTilLogin.error = getString(R.string.invalid_email_address)
            binding.emailTilLogin.endIconDrawable = null
        } else {
            binding.emailTilLogin.apply {
                error = null
                endIconDrawable = checkIcon
                setStartIconDrawable(R.drawable.baseline_check_24)
                setStartIconTintList(ColorStateList.valueOf(Color.GREEN))
            }
        }
        return binding.emailTilLogin.error == null
    }


    private fun validatePassword(): Boolean {
        val password = binding.edPasswordLogin.text.toString().trim()
        if (!password.isPasswordValid()) {
            binding.passworrdTilLogin.error = getString(R.string.oops_your_password_is_not_correct)
            binding.passworrdTilLogin.endIconDrawable = null
        } else {
            binding.passworrdTilLogin.apply {
                error = null
                endIconDrawable = checkIcon
                setStartIconDrawable(R.drawable.baseline_check_24)
                setStartIconTintList(ColorStateList.valueOf(Color.GREEN))
            }
        }
        return binding.passworrdTilLogin.error == null
    }


    private fun emailFocusListener() {
        val emailValue = binding.edEmailLogin
        emailValue.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                validateEmail()
            }
        }
    }

    private fun passwordFocusListener() {
        val passwordValue = binding.edPasswordLogin
        passwordValue.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                validatePassword()
            }
        }
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

    override fun afterTextChanged(s: Editable?) {
        binding.btnLogin.isEnabled =
            binding.edEmailLogin.text!!.trim().toString().isNotEmpty()
                    && binding.edPasswordLogin.text.toString().isNotEmpty()
                    && validateEmail()
                    && validatePassword()
    }

    //==================================================================================================
    private fun initListener() {
        navigateToHome()
        navigateToRegister()
        navigateToForgetPassword()

        binding.btnGoogle.setOnClickListener {
            loginWithGoogleRequest()
            navigateToHome()
        }
        binding.btnFacebook.setOnClickListener {
            loginWithFacebook()
        }
    }

    private fun navigateToHome() {

        binding.btnLogin.setOnClickListener { v ->
            Navigation.findNavController(v).navigate(R.id.action_loginFragment_to_homeFragment)
        }
    }

    private fun navigateToRegister() {
        binding.btnCreateCount.setOnClickListener { v ->
            Navigation.findNavController(v).navigate(R.id.action_loginFragment_to_registerFragment)
        }
    }

    private fun navigateToForgetPassword() {
        binding.btnForgetPasswrod.setOnClickListener {
            showForgetPasswordBottomSheet()
        }
    }

    private fun showForgetPasswordBottomSheet() {
        val forgetPasswordFragment = ForgetPasswordFragment()
        forgetPasswordFragment.show(parentFragmentManager, forgetPasswordFragment.tag)
    }

    companion object {
        const val TAG = "Login"
    }

}