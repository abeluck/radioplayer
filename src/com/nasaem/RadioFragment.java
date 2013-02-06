package com.nasaem;

import net.sourceforge.servestream.bean.UriBean;
import net.sourceforge.servestream.transport.AbsTransport;
import net.sourceforge.servestream.transport.TransportFactory;
import net.sourceforge.servestream.utils.DetermineActionTask;
import net.sourceforge.servestream.utils.MusicUtils;
import net.sourceforge.servestream.utils.PreferenceConstants;
import net.sourceforge.servestream.utils.MusicUtils.ServiceToken;

import org.holoeverywhere.LayoutInflater;
import org.holoeverywhere.app.Fragment;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class RadioFragment extends Fragment implements OnClickListener, DetermineActionTask.MusicRetrieverPreparedListener {

	ImageView mPlayButton;
	private DetermineActionTask mDetermineActionTask;

//	mPlayButton = (ImageView) view.findViewById(R.id.playRadio);
//	mPlayButton.setOnClickListener( this );


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_listen, container, false);
		return view;
	}

	@Override
	public void onClick(View v) {
		Uri uri = TransportFactory.getUri("http://s6.voscast.com:7632");
		UriBean uriBean = TransportFactory.getTransport(uri.getScheme()).createUri(uri);
		AbsTransport transport = TransportFactory.getTransport(uriBean.getProtocol());
		transport.setUri(uriBean);
	    mDetermineActionTask = new DetermineActionTask(getActivity(), uriBean, this);
	    mDetermineActionTask.execute();

	}

	@Override
	public void onMusicRetrieverPrepared(String action, UriBean uri, long[] list) {
		if (action.equals(DetermineActionTask.URL_ACTION_PLAY)) {
			for(long i : list) {
	    		Log.d("onMusicRetrieverPrepared", "fucking list" + i);
	    	}
			MusicUtils.playAll(getActivity(), list, 0);
		}

	}

}
