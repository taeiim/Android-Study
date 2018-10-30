package com.androidhuman.example.simplegithub.ui.main

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.androidhuman.example.simplegithub.R
import com.androidhuman.example.simplegithub.api.model.GithubRepo
import com.androidhuman.example.simplegithub.data.provideSearchHistoryDao
import com.androidhuman.example.simplegithub.extensions.plusAssign
import com.androidhuman.example.simplegithub.extensions.runOnIoScheduler
import com.androidhuman.example.simplegithub.rx.AutoActivatedDisposable
import com.androidhuman.example.simplegithub.rx.AutoClearedDisposable
import com.androidhuman.example.simplegithub.ui.repo.RepositoryActivity
import com.androidhuman.example.simplegithub.ui.search.SearchActivity
import com.androidhuman.example.simplegithub.ui.search.SearchAdapter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.startActivity

class MainActivity : AppCompatActivity(), SearchAdapter.ItemClickListener {

    internal val adapter by lazy {
        SearchAdapter().apply { setItemClickListener(this@MainActivity) }
    }

    internal val searchHistoryDao by lazy { provideSearchHistoryDao(this) }

    internal val disposables = AutoClearedDisposable(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        lifecycle += disposables
        lifecycle += AutoActivatedDisposable(this) { fetchSearchHistory() }

        btnActivityMainSearch.setOnClickListener {
            startActivity<SearchActivity>()
        }

        with(rvActivityMainList) {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = this@MainActivity.adapter
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_activity_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (R.id.menu_activity_main_clear_all == item.itemId) {
            clearAll()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onItemClick(repository: GithubRepo) {
        startActivity<RepositoryActivity>(
                RepositoryActivity.KEY_USER_LOGIN to repository.owner.login,
                RepositoryActivity.KEY_REPO_NAME to repository.name)
    }

    private fun fetchSearchHistory(): Disposable
            = searchHistoryDao.getHistory()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ items ->
                with(adapter) {
                    setItems(items)
                    notifyDataSetChanged()
                }

                if (items.isEmpty()) {
                    showMessage(getString(R.string.no_recent_repositories))
                } else {
                    hideMessage()
                }
            }) {
                showMessage(it.message)
            }

    private fun clearAll() {
        disposables += runOnIoScheduler { searchHistoryDao.clearAll() }
    }

    private fun showMessage(message: String?) {
        with(tvActivityMainMessage) {
            text = message ?: "Unexpected error."
            visibility = View.VISIBLE
        }
    }

    private fun hideMessage() {
        with(tvActivityMainMessage) {
            text = ""
            visibility = View.GONE
        }
    }
}
