package gst.trainingcourse.trelloapp.view.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import gst.trainingcourse.trelloapp.R
import gst.trainingcourse.trelloapp.model.Board
import gst.trainingcourse.trelloapp.model.User
import gst.trainingcourse.trelloapp.model.UserBoard
import gst.trainingcourse.trelloapp.model.Workspace

class HomeAdapter(private val iOnClickItemBoard: IOnClickItemBoard) : RecyclerView.Adapter<HomeAdapter.ViewHolder>() {


    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var tvBoardName: TextView? = null

        init {
            tvBoardName = view.findViewById(R.id.tv_board_name)
            view.setOnClickListener {


            }
        }
    }

    private var boardList: MutableList<Board> = mutableListOf<Board>()

    /**
     * function to set data workspace list
     */
    fun setData(boardList: MutableList<Board>) {
        this.boardList.clear()
        this.boardList = boardList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.board_card, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val board: Board = boardList[position]
        holder.tvBoardName?.text = board.name
        holder.itemView.setOnClickListener() {
            iOnClickItemBoard.iOnClickItem(boardList[position].id)
        }
    }

    override fun getItemCount(): Int {
        return boardList.size
    }

    interface IOnClickItemBoard {
        fun iOnClickItem(boardId: Int)
    }

}