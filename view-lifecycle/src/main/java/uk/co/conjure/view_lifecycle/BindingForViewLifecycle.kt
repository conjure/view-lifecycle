package uk.co.conjure.view_lifecycle

import androidx.fragment.app.Fragment
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

/**
 * A function to be used with by delegation which will ensure that the fragments reference to its view
 * binding is held only as long as the view lifecycle is alive. If you neglect to clear reference to
 * the view binding when the fragment is moved to the background you get a potentially costly memory leak.
 * This function can be used to automate this reference management like so:
 *
 * class MyFragment : Fragment() {
 *      private var binding by bindingForViewLifecycle<MyFragmentBinding>()
 *
 *      override fun onCreateView(
 *          inflater: LayoutInflater,
 *          container: ViewGroup?,
 *          savedInstanceState: Bundle?
 *      ): View {
 *          binding = MyFragmentBinding.inflate(inflater, container, false)
 *          return binding.root
 *      }
 * }
 *
 * This simplifies the often suggested method of managing this memory leak by setting the binding
 * to null in onDestroyView like so:
 *
 * class MyFragment : Fragment() {
 *
 *      private var binding: MyFragmentBinding? = null
 *
 *      override fun onCreateView(
 *          inflater: LayoutInflater,
 *          container: ViewGroup?,
 *          savedInstanceState: Bundle?
 *      ): View {
 *          binding = MyFragmentBinding.inflate(inflater, container, false)
 *          return binding!!.root
 *      }
 *
 *      override fun onDestroyView() {
 *          super.onDestroyView()
 *          binding = null
 *      }
 * }
 *
 * This method is provided for convenience in situations where [LifecycleView] is overkill
 * but in general [LifecycleView] should be the preferred solution as it also helps to separate
 * view logic from Fragment or Activity level concerns like navigation.
 *
 */
fun <T> Fragment.bindingForViewLifecycle(): ReadWriteProperty<Fragment, T> =
    object : ReadWriteProperty<Fragment, T>, DefaultLifecycleObserver {

        // A backing property to hold our value
        private var binding: T? = null

        init {
            // Observe the View Lifecycle of the Fragment
            this@bindingForViewLifecycle
                .viewLifecycleOwnerLiveData
                .observe(this@bindingForViewLifecycle) {
                    it.lifecycle.addObserver(this)
                }
        }

        //This should be called onDestroyView of the fragment
        override fun onDestroy(owner: LifecycleOwner) {
            super.onDestroy(owner)
            binding = null
        }

        override fun getValue(
            thisRef: Fragment,
            property: KProperty<*>
        ): T {
            // Return the backing property if it's set
            return this.binding!!
        }

        override fun setValue(
            thisRef: Fragment,
            property: KProperty<*>,
            value: T
        ) {
            // Set the backing property
            this.binding = value
        }
    }
