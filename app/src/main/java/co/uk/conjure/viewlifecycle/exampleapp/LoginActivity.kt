package co.uk.conjure.viewlifecycle.exampleapp

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import co.uk.conjure.viewlifecycle.exampleapp.databinding.ActivityLoginBinding
import io.reactivex.rxjava3.disposables.CompositeDisposable

/**
 * The LoginActivity creates the ViewModel and is hosting the [LoginFragment].
 * It's only other responsibility is to navigate to the Dashboard when the login succeeds.
 */
class LoginActivity : AppCompatActivity() {

    private val subscriptions = CompositeDisposable()

    private val viewModel: LoginViewModelImpl by viewModels { LoginViewModelImpl.Factory }

    private val binding: ActivityLoginBinding by lazy {
        ActivityLoginBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        viewModel.run { /* Initialize the lazy VM so it's ready for Fragments */ }
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }

    override fun onStart() {
        super.onStart()
        subscriptions.add(
            viewModel.onLoginComplete.subscribe { onLoginComplete() }
        )
    }

    override fun onStop() {
        super.onStop()
        subscriptions.clear()
    }

    /**
     * Here you would usually launch a DashboardActivity and finish this Activity.
     * We just show a text "LOGIN COMPLETE".
     */
    private fun onLoginComplete() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, LoggedInFragment())
            .commit()
    }
}