package nun.hunhun.cinevie.ui.detail

import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_choose_seat.*
import kotlinx.android.synthetic.main.activity_detail_film.*
import kotlinx.android.synthetic.main.activity_detail_film.view.*
import kotlinx.android.synthetic.main.fragment_dashboard.*
import nun.hunhun.cinevie.R
import nun.hunhun.cinevie.model.Cast
import nun.hunhun.cinevie.model.Film
import nun.hunhun.cinevie.ui.checkout.seat.ChooseSeatActivity

class FilmDetailActivity : AppCompatActivity() {

    private lateinit var reference : DatabaseReference
    private lateinit var data: Film
    private var castList = ArrayList<Cast>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_film)

        data = intent.getParcelableExtra("data")!!

        reference = FirebaseDatabase.getInstance().getReference("Film").child(data.title.toString()).child("cast")

        rv_cast.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        initView()
        getDataCast()
    }

    private fun initView(){
        val btnBack = findViewById<LinearLayout>(R.id.btn_back)

        tv_title.text = data.title
        tv_genre.text = data.genre
        tv_rating.text = data.rating
        tv_desc.text = data.desc
        Glide.with(this).load(data.poster).into(img_poster)

        btn_buy.setOnClickListener { startActivity(Intent(this, ChooseSeatActivity::class.java).putExtra("data", data)) }

        btnBack.setOnClickListener { onBackPressed() }
    }

    private fun getDataCast(){
        reference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                castList.clear()
                for (castSnapshot in snapshot.children) {
                    val cast = castSnapshot.getValue(Cast::class.java)

                    if (cast != null) {
                        castList.add(cast)
                    }

                    rv_cast.adapter = CastAdapter(castList)
                }
            }

            override fun onCancelled(error: DatabaseError) { Toast.makeText(this@FilmDetailActivity, "" + error.message, Toast.LENGTH_LONG).show() }
        })
    }
}