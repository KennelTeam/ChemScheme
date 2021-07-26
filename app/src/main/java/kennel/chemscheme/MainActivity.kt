package kennel.chemscheme

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import com.badlogic.gdx.backends.android.AndroidFragmentApplication

class MainActivity : AppCompatActivity(), AndroidFragmentApplication.Callbacks {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun exit() {
        TODO("Not yet implemented")
    }
}