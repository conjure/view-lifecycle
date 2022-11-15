package co.uk.conjure.viewlifecycle.exampleapp

import android.view.View
import co.uk.conjure.viewlifecycle.exampleapp.databinding.FragmentLoginBinding
import com.jakewharton.rxbinding4.view.clicks
import com.jakewharton.rxbinding4.widget.textChanges
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.addTo
import uk.co.conjure.view_lifecycle.LifecycleView

/**
 * The LoginView is responsible to bind the layout to the ViewModel.
 *
 */
class LoginView : LifecycleView<FragmentLoginBinding>() {

    private val subscriptions = CompositeDisposable()

    lateinit var viewModel: LoginViewModel

    override fun onStart() {
        super.onStart()

        binding.etEmail.textChanges()
            .map { it.toString() }
            .subscribe(viewModel.email)
        binding.etPassword.textChanges()
            .map { it.toString() }
            .subscribe(viewModel.password)
        binding.btnLogin.clicks()
            .subscribe(viewModel.loginClick)

        viewModel.isLoginButtonEnabled
            .subscribe { binding.btnLogin.isEnabled = it }
            .addTo(subscriptions)

        viewModel.isLoading.subscribe { loading ->
            if (loading) {
                binding.etEmail.isEnabled = false
                binding.etPassword.isEnabled = false
                binding.pbLoading.visibility = View.VISIBLE
            } else {
                binding.etEmail.isEnabled = true
                binding.etPassword.isEnabled = true
                binding.pbLoading.visibility = View.GONE
            }
        }.addTo(subscriptions)
    }

    override fun onStop() {
        super.onStop()
        subscriptions.clear()
    }
}