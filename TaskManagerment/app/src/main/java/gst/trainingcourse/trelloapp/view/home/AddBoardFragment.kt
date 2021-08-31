package gst.trainingcourse.trelloapp.view.home

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import gst.trainingcourse.trelloapp.R
import gst.trainingcourse.trelloapp.base.BaseFragment
import gst.trainingcourse.trelloapp.databinding.FragmentAddBoardBinding
import gst.trainingcourse.trelloapp.model.Board
import gst.trainingcourse.trelloapp.model.Workspace
import gst.trainingcourse.trelloapp.request.BoardRequest
import gst.trainingcourse.trelloapp.request.WorkspaceRequest
import gst.trainingcourse.trelloapp.utils.Constants.BOARD_ID
import gst.trainingcourse.trelloapp.view.MainActivity
import java.util.*


class AddBoardFragment : BaseFragment<FragmentAddBoardBinding>() {


    private var etAddBoard: EditText? = null

    private var workspacesList = mutableListOf<Workspace>()

    private var userId: Int? = null

    private var workspaceName = mutableListOf<String>()

    private var listBoard = mutableListOf<Board>()





    private fun getData() {
        workspacesList.clear()
        userId?.let {
            WorkspaceRequest.getWorkspaceByUser(it) { list ->
                if (list != null) {
                    workspacesList = list
                    val handler = Handler(Looper.getMainLooper())
                    handler.postDelayed({
                        for (document in workspacesList) {
                            workspaceName.add(document.name)
                            break
                        }
                    }, 200)
                }
            }
        }
    }

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): FragmentAddBoardBinding {
        return FragmentAddBoardBinding.inflate(inflater, container, false)
    }

    override fun initView() {
        //show action bar
        (activity as MainActivity).supportActionBar?.show()
        // Set title action bar
        (context as AppCompatActivity).supportActionBar?.title =
            getString(R.string.add_board_bar_title)
        userId = arguments?.getInt("userId")
        getData()
        etAddBoard = view?.findViewById(R.id.et_board_name)
    }

    /**
     * override fun initAction from Base class.
     */
    override fun initAction() {
        binding.btnCreateBoard.setOnClickListener {
            addBoard()
        }
    }


    private fun addBoard() {
        val createdTime = Calendar.getInstance().time.toString()
        val name = etAddBoard?.text?.trim().toString()
        var workspaceId: Int = 0
        var boardId = 0
        for (document in workspacesList) {
            workspaceId = document.id
            break
        }

        userId?.let {
            BoardRequest.createBoard(it,Board(0, workspaceId, name, createdTime)) {
                    board -> if ( board != null) {
                boardId = board.id
                val bundle = Bundle()
                bundle.putInt(BOARD_ID, boardId)
                Log.i("Test333", "$boardId")
                view?.findNavController()?.navigate(R.id.action_addBoardFragment_to_boardFragment, bundle)
            }
            }
        }

//        UserBoardRequest.createUserBoard(UserBoard(0, userId, boardId)) {
//
//        }

    }

}