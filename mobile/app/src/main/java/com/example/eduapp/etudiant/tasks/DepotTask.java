package com.example.eduapp.etudiant.tasks;//package com.example.eduapp.tasks;
//
//import android.os.AsyncTask;
//
//import com.example.eduapp.models.Depot;
//
//import java.io.File;
//    public class DepotTask extends AsyncTask<Void, Void, Boolean> {
//
//        private final Depot depot;
//        private final OnDepotCompletedListener listener;
//
//        public DepotTask(Depot depot, OnDepotCompletedListener listener) {
//            this.depot = depot;
//            this.listener = listener;
//        }
//
//        @Override
//        protected Boolean doInBackground(String... params) {
//
//            Depot depot = new Depot(params[1], params[2]);
////            File file = new File(params[0]);
////            depot.setFile(file);
////            depot.setAssignment_id(Integer.parseInt(params[1]));
////            depot.setEtudiant_id(Integer.parseInt(params[2]));
//
//
//            return null;
//        }
//
//        @Override
//        protected void onPostExecute(Boolean success) {
//            listener.onDepotCompleted(success);
//        }
//
//
//    }
//
//
