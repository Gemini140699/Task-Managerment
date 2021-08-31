package gst.trainingcourse.trelloapp.view.home

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.navigation.NavigationView
import de.hdodenhof.circleimageview.CircleImageView
import gst.trainingcourse.trelloapp.R
import gst.trainingcourse.trelloapp.base.BaseFragment
import gst.trainingcourse.trelloapp.databinding.FragmentHomeBinding
import gst.trainingcourse.trelloapp.model.Board
import gst.trainingcourse.trelloapp.model.User
import gst.trainingcourse.trelloapp.model.UserBoard
import gst.trainingcourse.trelloapp.request.BoardRequest
import gst.trainingcourse.trelloapp.request.UserBoardRequest
import gst.trainingcourse.trelloapp.request.UserRequest
import gst.trainingcourse.trelloapp.request.WorkspaceRequest
import gst.trainingcourse.trelloapp.utils.Constants.BOARD_ID
import gst.trainingcourse.trelloapp.utils.Constants.DATA_USER
import gst.trainingcourse.trelloapp.view.MainActivity
import gst.trainingcourse.trelloapp.viewmodel.login.ValidateViewModel


class HomeFragment : BaseFragment<FragmentHomeBinding>() {

    private var layoutManager: RecyclerView.LayoutManager? = null

    private var homeAdapter: HomeAdapter? = null

    private var recyclerView: RecyclerView? = null

    private var boardList = mutableListOf<Board>()

    private var userBoardList = mutableListOf<UserBoard>()

    private var userInfo = MainActivity.user

    private val listener = object : HomeAdapter.IOnClickItemBoard {
        override fun iOnClickItem(boardId: Int) {
            val bundle = Bundle()
            bundle.putInt(BOARD_ID, boardId)
            view?.findNavController()?.navigate(R.id.action_homeFragment_to_boardFragment, bundle)
        }

    }

    private fun getData() {
        userBoardList.clear()
        userInfo?.id?.let {
            UserBoardRequest.getBoardByUser(it) { mUserBoardList ->
                if (mUserBoardList != null) {
                    for (document in mUserBoardList) {
                        Log.d("HuyNv31", "on userBoard, BoardId is ${document.boardId}")
                    }
                }
                if (mUserBoardList != null) {
                    userBoardList = mUserBoardList
                    setData(userBoardList)
                }
            }
        }
    }

    private fun setData(userBoardList: MutableList<UserBoard>) {
        boardList.clear()
        for (userBoard in userBoardList) {
            BoardRequest.getBoardById(userBoard.boardId) { board ->
                Log.d("HuyNv31", "${board?.workspaceId}")
                if (board != null) {
                    boardList.add(board)
                }
            }
        }
// delay to load adapter
        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed({
            homeAdapter?.setData(boardList)
            recyclerView?.adapter = homeAdapter
        }, 1000)


    }

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): FragmentHomeBinding {
        return FragmentHomeBinding.inflate(inflater, container, false)
    }

    override fun initView() {

        (activity as MainActivity).supportActionBar?.show()
        // Set title action bar
        (context as AppCompatActivity).supportActionBar?.title =
            getString(R.string.home_bar_title)
        setUpNavDrawerInfo()
    }

    override fun initData() {
        getUserInfo()
        recyclerView = view?.findViewById(R.id.recyclerView)
        layoutManager = LinearLayoutManager(context)
        recyclerView?.layoutManager = layoutManager
        homeAdapter = HomeAdapter(listener)
        getData()
    }

    private fun getUserInfo() {
        if (userInfo == null) {
            userInfo = arguments?.getSerializable(DATA_USER) as User
            setUpNavDrawerInfo()
        }
    }

    override fun initAction() {
        val bundle = Bundle()
        bundle.putInt("userId", userInfo?.id!!)
        binding.apply {
            btnAddBoard.setOnClickListener {
                view?.findNavController()
                    ?.navigate(R.id.action_homeFragment_to_addBoardFragment, bundle)
            }
        }
    }

    private fun setUpNavDrawerInfo() {
        val headerView = activity?.findViewById<NavigationView>(R.id.navView)?.getHeaderView(0)
        val avatar = headerView?.findViewById<CircleImageView>(R.id.im_avatar)
        val tvName = headerView?.findViewById<TextView>(R.id.tv_name)
        val tvUserName = headerView?.findViewById<TextView>(R.id.tv_user_name)
        val email = headerView?.findViewById<TextView>(R.id.tv_email)
        if (userInfo != null) {
            loadAlreadyUserInfo(avatar, tvName, tvUserName, email)
        }
    }

    private fun loadAlreadyUserInfo(
        avatar: CircleImageView?,
        tvName: TextView?,
        tvUserName: TextView?,
        email: TextView?
    ) {
        if (userInfo?.avatar?.isEmpty() == true) {
            Glide.with(context).load(R.drawable.ic_user_avatar)
                .into(avatar)
        } else {
            Glide.with(context).load(userInfo?.avatar?.toUri()).error(R.drawable.ic_user_avatar)
                .into(avatar)
        }
        tvName?.text = userInfo?.name
        tvUserName?.text = userInfo?.accountName
        email?.text = userInfo?.email
    }


}

