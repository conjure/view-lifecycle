# view-lifecycle
[![](https://jitpack.io/v/conjure/view-lifecycle.svg)](https://jitpack.io/#conjure/view-lifecycle)

A small android library to help reduce fragment view resource leaks and separate view related code cleanly. See the [RxLifecycle library for some helpful rx java/kotlin extensions](https://github.com/conjure/RxLifecycle)

## Including the library

First add the following to your project level gradle repositories:

```gradle
allprojects {
	repositories {
		...
		maven { url 'https://jitpack.io' }
	}
}
```

Then add the view-lifecycle dependency in the module level build.gradle:

```gradle
dependencies {
	implementation 'uk.co.conjure:view-lifecycle:1.0.0-alpha01'
}
```

## Motivation

A common issue with fragments in android is the risk of creating a memory leak by holding reference to your view binding longer than you should. This is because fragments actually have two lifecycles, the fragment lifecycle and the view lifecycle, and the fragment can live in the background much longer than the view is required. The typical solution to this problem is to create your binding in `onCreateView` and set it to null in `onDestroyView` like so: 

```kotlin
class MyFragment : Fragment() {

     private var binding: MyFragmentBinding? = null

     override fun onCreateView(
         inflater: LayoutInflater,
         container: ViewGroup?,
         savedInstanceState: Bundle?
     ): View {
         binding = MyFragmentBinding.inflate(inflater, container, false)
         return binding!!.root
     }

     override fun onDestroyView() {
         super.onDestroyView()
         binding = null
     }
}
```

However there are some problems here: 

1. We need boilerplate code in every fragment to clean up the memory leak.
2. This boilerplate code, along with other fragment level concerns like navigation, end up mixed with your view logic. This is poor separation of concerns.

## Code Sample

To fix these problems the view-lifecycle library provides two different solutions.

First consider writing a `LifecycleView` to hold your view logic like so: 

```kotlin
class MyView : LifecycleView<FragmentMyViewBinding>() {
    lateinit var viewModel: MyViewModel

    override fun onStart() {
        super.onStart()
        //View logic goes here
    }

    override fun onStop() {
        super.onStop()
        //Clean up e.g. disposing event stream subscriptions goes here
    }
}
```

and then create an instance of your view in your fragment like so: 

```kotlin
class MyFragment : Fragment() {

    private lateinit var myView: MyView
    private val myViewModel: MyViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        myView = MyView().apply {
            viewModel = myViewModel
            registerBinding(
                FragmentMyViewBinding.inflate(inflater, container, false),
                this@MyFragment
            )
        }
        return myView.requireBinding().root
    }
}
```

The lifecycle view will call onStop on the view and dispose reference to the binding when `onDestroyView` is called (or `onDestroy` if the host is an activity). This also keeps your view logic separate from your fragment code.

Second For very simple fragments with little to no view logic you can use the `Fragment.bindingForViewLifecycle()` extension function like so: 

```kotlin
class MyFragment : Fragment() {
     private var binding by bindingForViewLifecycle<MyFragmentBinding>()

     override fun onCreateView(
         inflater: LayoutInflater,
         container: ViewGroup?,
         savedInstanceState: Bundle?
     ): View {
         binding = MyFragmentBinding.inflate(inflater, container, false)
         return binding.root
     }
}
```

This will also dispose reference to the binding when `onDestroyView` is called for you.
