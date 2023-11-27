package br.edu.scl.ifsp.sdm.contactlist.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ContextMenu
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView.AdapterContextMenuInfo
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import br.edu.scl.ifsp.sdm.contactlist.R
import br.edu.scl.ifsp.sdm.contactlist.adapter.ContactAdapter
import br.edu.scl.ifsp.sdm.contactlist.adapter.ContactRvAdapter
import br.edu.scl.ifsp.sdm.contactlist.databinding.ActivityMainBinding
import br.edu.scl.ifsp.sdm.contactlist.model.Constant.EXTRA_CONTACT
import br.edu.scl.ifsp.sdm.contactlist.model.Constant.EXTRA_VIEW_CONTACT
import br.edu.scl.ifsp.sdm.contactlist.model.Contact

class MainActivity : AppCompatActivity(), OnContactClickListener {
    private val amb: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    // Data Source
    private val contactList: MutableList<Contact> = mutableListOf()

    // Adapter
     private val contactAdapter: ContactRvAdapter by lazy {
//    private val contactAdapter: ArrayAdapter<String> by lazy{
        //ArrayAdapter(this,android.R.layout.simple_list_item_1, contactList.map{ it.toString()})
        ContactRvAdapter(contactList, this)


    }

    private lateinit var carl: ActivityResultLauncher<Intent>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(amb.root)
        setSupportActionBar(amb.toolbarIn.toolbar)
        supportActionBar?.subtitle = getString(R.string.contact_list)

        carl = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val contact = result.data?.getParcelableExtra<Contact>(EXTRA_CONTACT)
                contact?.also { newOrEditedContact ->
                    if (contactList.any { it.id == newOrEditedContact.id }) {
                        // Edição
                        val position = contactList.indexOfFirst { it.id == contact.id }
                        contactList[position] = newOrEditedContact
                        contactAdapter.notifyItemChanged(position)
                    } else {
                        contactList.add(newOrEditedContact)
                        contactAdapter.notifyItemInserted(contactList.lastIndex)
                    }
                   // contactAdapter.notifyDataSetChanged()
                }
            }
        }
        fillContacts()

        //amb.contactlsLv.adapter = contactAdapter
        amb.contactRv.adapter = contactAdapter
        amb.contactRv.layoutManager = LinearLayoutManager(this)
        //registerForContextMenu(amb.contactlsLv)

/*        amb.contactlsLv.setOnItemClickListener{ _, _, position, _->
            val contact = contactList[position]
            val viewContactIntent = Intent(this, ContactActivity::class.java)
            viewContactIntent.putExtra(EXTRA_CONTACT, contact)
            viewContactIntent.putExtra(EXTRA_VIEW_CONTACT, true)
            startActivity(viewContactIntent)
        }*/

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            R.id.addContactMi -> {
                carl.launch(Intent(this, ContactActivity::class.java))
                true
            }
            else -> {false}
        }


    }

/*    override fun onCreateContextMenu(
        menu: ContextMenu?,
        v: View?,
        menuInfo: ContextMenu.ContextMenuInfo?
    ) {
       //super.onCreateContextMenu(menu, v, menuInfo)
        menuInflater.inflate(R.menu.context_menu_main, menu)
    }*/

    override fun onRemoveContactMenuItemClick(position: Int) {
        contactList.removeAt(position)
        contactAdapter.notifyItemRemoved(position)
        Toast.makeText(this, "contact remove", Toast.LENGTH_SHORT).show()
    }

    override fun onEditContactMenuItemClick(position: Int) {
        carl.launch(Intent(this, ContactActivity::class.java).apply {
            putExtra(EXTRA_CONTACT, contactList[position])
        })
    }

/*    override fun onContextItemSelected(item: MenuItem): Boolean {
        //return super.onContextItemSelected(item)

        val position = (item.menuInfo as AdapterContextMenuInfo).position

        return when(item.itemId) {
            R.id.removeContactMi -> {
                contactList.removeAt(position)
                contactAdapter.notifyDataSetChanged()
                Toast.makeText(this, "contact remove", Toast.LENGTH_SHORT).show()
                true
            }
            R.id.editContactMi -> {
*//*                val contact = contactList[position]
                val editContactIntent = Intent(this, ContactActivity::class.java)
                editContactIntent.putExtra(EXTRA_CONTACT, contact)*//*

                carl.launch(Intent(this, ContactActivity::class.java).apply {
                    putExtra(EXTRA_CONTACT, contactList[position])
                })
                true
            }
            else -> { false }
        }
    }*/

    override fun onDestroy() {
        super.onDestroy()
        //unregisterForContextMenu(amb.contactlsLv)
    }

    override fun onContactClick(position: Int) {
        Intent(this,ContactActivity::class.java).apply {
            putExtra(EXTRA_CONTACT, contactList[position])
            putExtra(EXTRA_VIEW_CONTACT,true)
        }.also {
            startActivity(it)
        }
    }

    private fun fillContacts() {
        for ( i in 1..10) {
            contactList.add(
                Contact(
                    i,
                    name = "Nome $i",
                    address = "Endereco $i",
                    phone = "Telefone $i",
                    email = "Email $i"

                )
            )
        }
    }
}