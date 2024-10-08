package sih.binarylifters.serenitysync.adapters

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import sih.binarylifters.serenitysync.R
import sih.binarylifters.serenitysync.assessmentactivities.GADActivity
import sih.binarylifters.serenitysync.assessmentactivities.PHQActivity
import sih.binarylifters.serenitysync.classes.TestNames

class TestsAdapter(private val context: Context, private val testList: ArrayList<TestNames>) :
    RecyclerView.Adapter<TestsAdapter.TestViewHolder>() {

    class TestViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val testIcon = itemView.findViewById<ImageView>(R.id.iv_test_icon)
        val shortName = itemView.findViewById<TextView>(R.id.tv_short_name)
        val fullName = itemView.findViewById<TextView>(R.id.tv_full_name)
        val clMain = itemView.findViewById<ConstraintLayout>(R.id.cl_main)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TestViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_test, parent, false)
        return TestViewHolder(view)
    }

    override fun onBindViewHolder(holder: TestViewHolder, position: Int) {
        val data = testList[position]

        holder.shortName.text = data.shortForm
        holder.fullName.text = data.fullName
        val ovalDrawable = holder.testIcon.background as GradientDrawable
        ovalDrawable.setColor(Color.parseColor(data.color))

        holder.clMain.setOnClickListener {
            when(data.shortForm) {
                "PHQ-9" -> {
                    val intent = Intent(context, PHQActivity::class.java)
                    context.startActivity(intent)
                }
                "GAD-7" -> {
                    val intent = Intent(context, GADActivity::class.java)
                    context.startActivity(intent)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return testList.size
    }
}
