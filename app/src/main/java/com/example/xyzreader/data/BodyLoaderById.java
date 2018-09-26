package com.example.xyzreader.data;

public class BodyLoaderById {

//	class BodyLoader extends AsyncTask<Long, Void, String> {
//
//		@Override
//		protected String doInBackground(Long... params) {
//			return bookItemDatabase.bookItemDao().fetchBodyById((int) (long) params[0]);
//		}
//
//		@Override
//		protected void onPostExecute(String result) {
//			super.onPostExecute(result);
//			body = result;
//		}
//	}
	private final BookItemDatabase bookItemDatabase;
	private String body = "";

	public BodyLoaderById(BookItemDatabase bookItemDatabase) {
		this.bookItemDatabase = bookItemDatabase;
	}

	public String getBodyById(long id) {
//		new BodyLoader().execute(id);
		return body;
	}
}
