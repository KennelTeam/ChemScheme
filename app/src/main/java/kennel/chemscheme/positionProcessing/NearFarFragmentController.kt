package kennel.chemscheme.positionProcessing

import android.content.Context
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.MarginLayoutParams
import android.view.WindowManager
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.marginTop
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.badlogic.gdx.math.Vector2
import kennel.chemscheme.R
import kennel.chemscheme.databinding.FarnearFragmentBinding
import kennel.chemscheme.structure.MolStruct
import java.text.FieldPosition
import kotlin.random.Random


operator fun Vector2.plus(other : Vector2) = Vector2(x + other.x, y + other.y)
operator fun Vector2.minus(other : Vector2) = Vector2(x - other.x, y - other.y)
operator fun Vector2.times(k : Float) = Vector2(x * k, y * k)
operator fun Vector2.div(k : Float) = Vector2(x / k, y / k)


class NearFarFragmentController : Fragment() {
    private lateinit var binding: FarnearFragmentBinding
    private var parentLinearLayout: ConstraintLayout? = null
    private var random : Random = Random(1791791791)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.farnear_fragment,
            container,
            false
        )
        binding.buttonAdd.setOnClickListener { onAddField() }

        return binding.root
    }

    fun drawNearFarScheme(struct : Structure3D){

    }

    fun drawAtom(position: Vector2, atom : MolStruct.Elements){
        val inflater = activity?.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        val rowView: View = inflater.inflate(R.layout.atom_farnear, null)
//        rowView.scaleX = 100.0f
//        rowView.scaleY = 100.0f
        val text = TextView(context)
        binding.farnearLayout.addView(text)
        text.text = atom.name

        //text.scaleX = 0.01f
        //text.scaleY = 0.01f
        text.x = position.x// / 100
        text.y = position.y// / 100
    }

    fun drawConnection(from : Vector2, to : Vector2, connectionType : ConnectionType){
//        val inflater = activity?.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
//
//        val rowView: View = inflater.inflate(R.layout.toadd_near_far, null)
//        rowView.scaleX = 1.0f
//        rowView.scaleY = 1.0f
//        rowView.x = 0.0f
//        rowView.y = 0.0f
//
//
        val img = ImageView(context)
        binding.farnearLayout.addView(img)
        when(connectionType){
            ConnectionType.FAR -> img.setImageResource(R.drawable.connection_far)
            ConnectionType.NEAR -> img.setImageResource(R.drawable.connection_near)
            ConnectionType.SIMPLE -> img.setImageResource(R.drawable.connection_simple)
        }
        img.y = 50f
//        img.layoutParams.width =
//        img.layoutParams.height = 200
//        img.scaleType = ImageView.ScaleType.FIT_XY
//        img.scaleX = 200f
//        img.scaleY = 50f
  //      img.layoutParams.width =
//
//        val diff = to - from
//        val size = diff.len() * 0.8f
//
//        var dx = img.drawable.intrinsicWidth.toFloat() / img.drawable.intrinsicHeight.toFloat() * size
//
//        var position : Vector2 = ((to + from) / 2.0f) - Vector2(0.0f, size / 2)
//        position = Vector2(0f,0.0f)
//
//        Log.i("pos", position.x.toString())
//
//        val params:  ViewGroup.LayoutParams = img.layoutParams as ViewGroup.LayoutParams
//        params.height = size.toInt()
//
//        img.x = position.x
//        img.scaleX = 1f
//        img.scaleY = 1f
//        img.y = position.y
//
//        //img.rotation = diff.angleDeg() + 90.0f
//
//
//        parentLinearLayout!!.addView(rowView, parentLinearLayout!!.childCount - 1)
    }

    fun onDelete(view: View){
        parentLinearLayout!!.removeView(view.parent as View)
    }

    fun onAddField(){
        drawConnection(Vector2(500.0f, 100.0f), Vector2(0.0f, 0.0f), ConnectionType.FAR)
        drawAtom(Vector2(500.0f, 100.0f), MolStruct.Elements.Br)
        drawAtom(Vector2(0.0f, 0.0f), MolStruct.Elements.C)
    }
}