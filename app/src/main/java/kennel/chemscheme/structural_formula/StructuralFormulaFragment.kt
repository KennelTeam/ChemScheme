package kennel.chemscheme.structural_formula

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import kennel.chemscheme.R
import kennel.chemscheme.databinding.StructuralFormulaFragmentBinding
import kotlin.math.*

class StructuralFormulaFragment: Fragment() {

    private lateinit var binding: StructuralFormulaFragmentBinding
    private lateinit var canvas: Canvas
    private lateinit var bitmap: Bitmap
    private val backgroundColor = Color.LTGRAY
    private val potentialAtomsColor = Color.GRAY
    private val atomColor = Color.BLACK

    private val atomList = arrayListOf<Atom>()
    private val atomRadius = 20f
    private var selectedAtom: Atom? = null

    private val paint = Paint().apply {
        color = atomColor
        style = Paint.Style.STROKE
        strokeWidth = 2f
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
                inflater,
                R.layout.structural_formula_fragment,
                container,
                false
        )

        bitmap = Bitmap.createBitmap(800, 800, Bitmap.Config.ARGB_8888)
        canvas = Canvas(bitmap)
        canvas.drawColor(backgroundColor)

        binding.mainImageView.setImageBitmap(bitmap)

        binding.mainImageView.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                if (selectedAtom != null) {
                    var atomClick = false
                    for (atom in atomList) {
                        if (distance(atom.x, event.x, atom.y, event.y) <= atomRadius) {
                            atomClick = true
                            if (atom != selectedAtom) { focusAtom(selectedAtom) }
                            focusAtom(atom)
                        }
                    }
                    if (!atomClick) {
                        canvas.drawLine(selectedAtom!!.x, selectedAtom!!.y, event.x, event.y, paint)
                        addAtom(event.x, event.y, selectedAtom!!)
                        focusAtom(selectedAtom)
                    }
                } else {
                    for (atom in atomList) {
                        if (distance(atom.x, event.x, atom.y, event.y) <= atomRadius) {
                            focusAtom(atom)
                        }
                    }
                }
            }
            true
        }

        addAtom(400f, 400f, null)

        return binding.root
    }

    private fun addAtom(x: Float, y: Float, newNeighbor: Atom?) {
        paint.style = Paint.Style.FILL
        paint.color = backgroundColor
        canvas.drawCircle(x, y, atomRadius, paint)
        paint.style = Paint.Style.STROKE
        paint.color = atomColor
        canvas.drawCircle(x, y, atomRadius, paint)
        atomList.add(Atom(x, y, false))
        binding.mainImageView.setImageBitmap(bitmap)
    }

    private fun focusAtom(atom: Atom?) {
        if (atom!!.focused) {
            paint.style = Paint.Style.FILL
            paint.color = backgroundColor
            canvas.drawCircle(atom.x, atom.y, atomRadius, paint)
            paint.color = atomColor
            paint.style = Paint.Style.STROKE
            canvas.drawCircle(atom.x, atom.y, atomRadius, paint)
            atom.focused = false
            selectedAtom = null
        } else {
            paint.style = Paint.Style.FILL
            canvas.drawCircle(atom.x, atom.y, atomRadius, paint)
            atom.focused = true
            selectedAtom = atom
        }
        binding.mainImageView.setImageBitmap(bitmap)
    }

    private fun distance(x1: Float, x2: Float, y1: Float, y2: Float): Float {
        return sqrt(((x1 - x2).pow(2)) + ((y1 - y2).pow(2)))
    }
}

data class Atom(val x: Float, val y: Float, var focused: Boolean)