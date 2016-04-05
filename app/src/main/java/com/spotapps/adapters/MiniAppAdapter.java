package com.spotapps.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.spotapps.beans.MiniApp;

/**
 * Created by tty on 4/5/2015.
 */
public class MiniAppAdapter extends ArrayAdapter<MiniApp> {

    Context ctx;
    // @layoutResourceId - the listview_item_row.xml
    // int layoutResourceId;

    // @data - the ListItem data
    MiniApp data[] = null;







        public MiniAppAdapter(Context ctx, int layoutResourceId, MiniApp[] data) {

            super(ctx, layoutResourceId, data);
//            this.layoutResourceId = layoutResourceId;
            this.ctx = ctx;
            this.data = data;
        }


        // @We'll override the getView method which is called for every ListItem we have.
        // @There are lots of different caching techniques for Android ListView to
        // achieve better performance especially if you are going to have a very long ListView.

        // @convertView - the cache of list item row layout, if it is null, inflate new

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
/*
            if(convertView==null){
                // inflate the listview_item_row.xml parent
                LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
                convertView = inflater.inflate(layoutResourceId, parent, false);
            }

            // get the elements in the layout
            ImageView imageViewFolderIcon = (ImageView) convertView.findViewById(R.id.imageViewFolderIcon);
            TextView textViewFolderName = (TextView) convertView.findViewById(R.id.textViewFolderName);
            TextView textViewFolderDescription = (TextView) convertView.findViewById(R.id.textViewFolderDescription);


            // Set the data for the list item. You can also set tags here if you want.

            Folder folder = data[position];

            imageViewFolderIcon.setImageResource(folder.folderIcon);
            textViewFolderName.setText(folder.folderName);
            textViewFolderDescription.setText(folder.folderDescription);

            return convertView;

      */
            return null;
        }

    }

