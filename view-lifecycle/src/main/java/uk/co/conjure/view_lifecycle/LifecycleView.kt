package uk.co.conjure.view_lifecycle

import androidx.annotation.CallSuper
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.viewbinding.ViewBinding

/**
 * A Lifecycle aware class to separate View logic from Fragment or Activity level concerns like navigation.
 * Use [registerBinding] in an Activities onCreate() or Fragments onCreateView() to set the binding.
 * Then implement the lifecycle methods [onCreate], [onStart], [onStop], [onDestroy] to handle the lifecycle events for the View.
 *
 *
 * ### Code sample
 * Example of a Fragment implementing a Counter
 * ```
 * class CounterFragment : Fragment() {
 *
 *	    private lateinit var counterView: CounterView
 *
 *	    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
 *	        counterView = CounterView()
 *	        counterView.registerBinding(CounterViewBinding.inflate(inflater, container, false),this)
 *	        return counterView.requireBinding().root
 *	    }
 *  }
 * ```
 * And the View implementation
 *
 * ```
 * class CounterView : LifecycleView<CounterViewBinding>() {
 *
 *      var counter = 0
 *
 *	    override fun onCreate() {
 *	        super.onCreate()
 *
 *	        binding.btnIncrease.setOnClickListener {
 *		        counter++
 *		        binding.tvCounterValue.text = counter.toString()
 *	        }
 *	    }
 *	}
 * ```
 *
 * This pattern can reduce the size of Fragments and Activities by moving View logic into a separate class.
 * Activities and Fragments can then be used to handle navigation and other concerns.
 *
 * We recommend using ViewModels to handle the state of the View and communicate with the rest of the app.
 */
abstract class LifecycleView<B : ViewBinding> : ViewBindingHolder<B>() {

    // This is to ensure a sub class calls super.onStart() and super.onStop()
    private var called = false

    final override fun onBindingRegistered(binding: B, owner: LifecycleOwner) {
        owner.lifecycle.addObserver(object : DefaultLifecycleObserver {
            override fun onCreate(owner: LifecycleOwner) {
                called = false
                onCreate()
                if (!called) {
                    throw IllegalStateException("View $this did not call through to super.onCreate()")
                }
            }

            override fun onStart(owner: LifecycleOwner) {
                called = false
                onStart()
                if (!called) {
                    throw IllegalStateException("View $this did not call through to super.onStart()")
                }
            }

            override fun onResume(owner: LifecycleOwner) {
                called = false
                onResume()
                if (!called) {
                    throw IllegalStateException("View $this did not call through to super.onStart()")
                }
            }

            override fun onPause(owner: LifecycleOwner) {
                called = false
                onPause()
                if (!called) {
                    throw IllegalStateException("View $this did not call through to super.onStart()")
                }
            }

            override fun onStop(owner: LifecycleOwner) {
                called = false
                onStop()
                if (!called) {
                    throw IllegalStateException("View $this did not call through to super.onStop()")
                }
            }

            override fun onDestroy(owner: LifecycleOwner) {
                owner.lifecycle.removeObserver(this)
                called = false
                onDestroy()
                if (!called) {
                    throw IllegalStateException("View $this did not call through to super.onDestroy()")
                }
            }
        })
    }

    /**
     * Called when the view is created.
     *
     * Matches the [androidx.lifecycle.Lifecycle.Event.ON_CREATE] event.
     */
    @CallSuper
    open fun onCreate() {
        called = true
    }

    /**
     * Called when the view is started.
     *
     * Matches the [androidx.lifecycle.Lifecycle.Event.ON_START] event.
     */
    @CallSuper
    open fun onStart() {
        called = true
    }

    /**
     * Called when the view is resumed.
     *
     * Matches the [androidx.lifecycle.Lifecycle.Event.ON_RESUME] event.
     */
    @CallSuper
    open fun onResume() {
        called = true
    }

    /**
     * Called when the view is paused.
     *
     * Matches the [androidx.lifecycle.Lifecycle.Event.ON_PAUSE] event.
     */
    @CallSuper
    open fun onPause() {
        called = true
    }

    /**
     * Called when the view is stopped.
     *
     * Matches the [androidx.lifecycle.Lifecycle.Event.ON_STOP] event.
     */
    @CallSuper
    open fun onStop() {
        called = true
    }

    /**
     * Called when the view is destroyed.
     *
     * Matches the [androidx.lifecycle.Lifecycle.Event.ON_DESTROY] event.
     */
    @CallSuper
    open fun onDestroy() {
        called = true
    }
}