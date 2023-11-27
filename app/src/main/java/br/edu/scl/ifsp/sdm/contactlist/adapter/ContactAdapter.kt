package br.edu.scl.ifsp.sdm.contactlist.adapter

import android.content.Context
import android.content.Context.LAYOUT_INFLATER_SERVICE
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import br.edu.scl.ifsp.sdm.contactlist.R
import br.edu.scl.ifsp.sdm.contactlist.databinding.TileContactBinding
import br.edu.scl.ifsp.sdm.contactlist.model.Contact

class ContactAdapter(context: Context, private val contactList: MutableList<Contact>):
    ArrayAdapter<Contact>(context, R.layout.tile_contact, contactList) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val contact = contactList[position]
        //var tcb: TileContactBinding? = null

        var contactTileView = convertView
        if ( contactTileView == null ) {
            val tcb = TileContactBinding.inflate(
                context.getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater,
                parent,
                false
            )
            contactTileView = tcb.root

            val tileContactHolder = TileContactHolder(tcb.nameTv,tcb.emailTv, tcb.phoneTv)
            contactTileView.tag = tileContactHolder
        }

        val holder = contactTileView.tag as TileContactHolder
        holder.nameTv.text = contact.name
        holder.emailTv.text = contact.email
        holder.phoneTv.text = contact.phone

        //contactTileView.findViewById<TextView>(R.id.nameTv).text = contact.name
        // O holder acima substitui essa instrução com findViewById para resolver
        // A questão de performance
        return contactTileView
    }

    private data class TileContactHolder(
        val nameTv: TextView,
        val emailTv: TextView,
        val phoneTv: TextView
    )
}