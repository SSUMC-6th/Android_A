import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ScrollView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.umc_6th.FloChartResult
import com.example.umc_6th.LockerAlbumRVAdapter
import com.example.umc_6th.LookView
import com.example.umc_6th.R
import com.example.umc_6th.Song
import com.example.umc_6th.SongDatabase
import com.example.umc_6th.SongRVAdapter
import com.example.umc_6th.SongService
import com.example.umc_6th.databinding.FragmentLookBinding

class LookFragment : Fragment(),LookView {

    lateinit var binding: FragmentLookBinding
    private lateinit var floCharAdapter: SongRVAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLookBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        getSongs()
    }

    private fun initRecyclerView(result: FloChartResult){
        floCharAdapter = SongRVAdapter(requireContext(), result)

        binding.lookFloChartRv.adapter = floCharAdapter
    }
    private fun getSongs() {
        val songService = SongService()
        songService.setLookView(this)

        songService.getSongs()

    }

    override fun onGetSongLoading() {
        binding.lookLoadingPb.visibility = View.VISIBLE
    }

    override fun onGetSongSuccess(code: Int, result: FloChartResult) {
        binding.lookLoadingPb.visibility = View.GONE
        initRecyclerView(result)
    }

    override fun onGetSongFailure(code: Int, message: String) {
        binding.lookLoadingPb.visibility = View.GONE
        Log.d("LOOK-FRAG/SONG-RESPONSE", message)
    }
}