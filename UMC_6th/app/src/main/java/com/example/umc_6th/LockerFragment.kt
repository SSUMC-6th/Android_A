package com.example.umc_6th

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.umc_6th.adapter.LockerPagerAdapter
import com.example.umc_6th.databinding.FragmentHomeBinding
import com.example.umc_6th.databinding.FragmentLockerBinding
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.tabs.TabLayoutMediator

class LockerFragment : Fragment() {
    private var _binding: FragmentLockerBinding? = null
    private val binding get() = _binding!!
    private val information = arrayListOf("저장한곡", "음악파일", "저장앨범")

    private lateinit var sharedViewModel: SharedViewModel
    private lateinit var bottomSheetDialog: BottomSheetDialog
    lateinit var songDB: SongDatabase

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLockerBinding.inflate(inflater, container, false)

        val lockerAdapter = LockerPagerAdapter(this)
        binding.vpLocker.adapter = lockerAdapter
        TabLayoutMediator(binding.tbLocker, binding.vpLocker) { tab, position ->
            tab.text = information[position]
        }.attach()

        //로그인 intent
        binding.txLogin.setOnClickListener {
            val intent = Intent(requireActivity(), LoginActivity::class.java)
            startActivity(intent)
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharedViewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)


        binding.txLockerSelectAll.setOnClickListener{
            showBottomSheetDialog()
        }
    }

    private fun showBottomSheetDialog() {
        bottomSheetDialog = BottomSheetDialog(requireContext())
        bottomSheetDialog.setContentView(R.layout.bottom_sheet_dialog)
        val btnDislike = bottomSheetDialog.findViewById<ImageView>(R.id.imgDialogDelete)
        btnDislike?.setOnClickListener {
            sharedViewModel.triggerDislikeAll()
            bottomSheetDialog.dismiss()  // Dialog 숨기기
        }
        bottomSheetDialog.show()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    class SharedViewModel : ViewModel() {
        private val _dislikeAllEvent = MutableLiveData<Boolean>()
        val dislikeAllEvent: LiveData<Boolean> = _dislikeAllEvent

        fun triggerDislikeAll() {
            _dislikeAllEvent.value = true
        }

        fun eventHandled() {
            _dislikeAllEvent.value = false
        }
    }

    override fun onStart() {
        super.onStart()
        initViews()
    }

    // 로그인 또는 로그아웃 기능
    private fun getJwt() : String? {
        val spf = requireActivity().getSharedPreferences("auth", AppCompatActivity.MODE_PRIVATE)
        return spf!!.getString("jwt", "")
    }

    private fun initViews() {
        val jwt: String = getJwt().toString()
        if (jwt == "") {
            binding.txLogin.text="로그인"
            binding.txLogin.setOnClickListener{
                startActivity(Intent(requireActivity(), LoginActivity::class.java))
            }
        }else {
            binding.txLogin.text="로그아웃"
            binding.txLogin.setOnClickListener{
                logout()
                startActivity(Intent(requireActivity(), MainActivity::class.java))
            }
        }
    }

    private fun logout() {
        val spf = activity?.getSharedPreferences("auth", AppCompatActivity.MODE_PRIVATE)
        val editor = spf!!.edit()
        editor.remove("jwt")
        editor.apply()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}