package com.example.realestate.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.realestate.R;
import com.example.realestate.adapter.HomeMenuListAdapter;
import com.example.realestate.model.HelpListMenuItem;
import com.example.realestate.model.ListMenuItem;

import java.util.ArrayList;
import java.util.List;

public class AboutHelpActivity extends LoggedInRequiredActivity implements AdapterView.OnItemClickListener {
	ListView mListView;
	TextView txtAbout;
	
	List<ListMenuItem> mItems;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
			
		setContentView(R.layout.activity_about_help);
		
		txtAbout = (TextView)findViewById(R.id.txtAbout);
		txtAbout.setText(Html.fromHtml(getString(R.string.about_content)));
		txtAbout.setMovementMethod (LinkMovementMethod.getInstance());
		
		mListView = (ListView)findViewById(R.id.listview);
		mListView.setOnItemClickListener(this);

		initAdapter();
	}
	
	@Override
	protected int getTitleResource() {
		return R.string.about_help;
	}

	private void initAdapter() {
		mItems = new ArrayList<ListMenuItem>();

		mItems.add(new HelpListMenuItem(R.string.help_general,
				R.drawable.saoviet_ic_help,
				R.string.help_general_content));

		mItems.add(new HelpListMenuItem(R.string.import_customers, 
				R.drawable.saoviet_ic_help,
				R.string.help_import_customer));

		mItems.add(new HelpListMenuItem(R.string.import_product_cats, 
				R.drawable.saoviet_ic_help,
				R.string.help_import_product_cat));

		mItems.add(new HelpListMenuItem(R.string.import_products, 
				R.drawable.saoviet_ic_help,
				R.string.help_import_product));

		mItems.add(new HelpListMenuItem(R.string.import_lines, 
				R.drawable.saoviet_ic_help,
				R.string.help_import_sale_line));

		mListView.setAdapter(new HomeMenuListAdapter(this, 0, mItems));
	}

	@Override
	public void onItemClick(AdapterView<?> group, View view, int position, long id) {
		HelpListMenuItem item = (HelpListMenuItem)mItems.get(position);
		
		Intent intent = new Intent(this, HelpTopicDetailActivity.class);
		intent.putExtra(HelpTopicDetailActivity.HELP_CONTENT, item.helpContentResId);
		intent.putExtra(HelpTopicDetailActivity.HELP_TITLE, item.titleResId);

		startActivity(intent);
	}
}
