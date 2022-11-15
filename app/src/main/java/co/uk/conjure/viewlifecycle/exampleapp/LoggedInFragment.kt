package co.uk.conjure.viewlifecycle.exampleapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import co.uk.conjure.viewlifecycle.exampleapp.databinding.FragmentLoggedInBinding
import uk.co.conjure.view_lifecycle.bindingForViewLifecycle

/**
 * Since this fragment contains no view logic it is simplest to manage the memory leak of the
 * binding using [bindingForViewLifecycle]
 *
 * @see bindingForViewLifecycle
 */
class LoggedInFragment : Fragment() {

    private var binding by bindingForViewLifecycle<FragmentLoggedInBinding>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoggedInBinding.inflate(inflater, container, false)
        return binding.root
    }
}