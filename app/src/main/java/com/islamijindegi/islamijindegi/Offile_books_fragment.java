package com.islamijindegi.islamijindegi;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class Offile_books_fragment extends Fragment {

    private List<OfflineBook> offlineBookList;

    ListView offlineBookListView;

    View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_offile_books_fragment,container,false);
        offlineBookListView=(ListView) view.findViewById(R.id.offlineBookListView);

        this.view=view;

        offline_books();

        return view;
    }

    public void offline_books(){

        offlineBookList = new ArrayList<>();

        String b[][]={
                {
                        "আমালুস সুন্নাহ",
                        "amalus_sunnah.pdf",
                        "মুফতী মনসূরুল হক দা.বা.",
                },

                {
                        "আহকামুত তালাক",
                        "ahakamut_talak.pdf",
                        "মুফতী মনসূরুল হক দা.বা.",
                },
                {
                        "আহকামে মাসাজিদ",
                        "ahakem_masjid.pdf",
                        "মুফতী মনসূরুল হক দা.বা.",
                },
                {
                        "ইশা‘আতুস সুন্নাহ",
                        "eshatus_sunnah.pdf",
                        "মুফতী মনসূরুল হক দা.বা.",
                },
                {
                        "ইসলামী খেলাফত",
                        "islami_khelafat.pdf",
                        "মুফতী মনসূরুল হক দা.বা.",
                },
                {
                        "ইসলামী বিবাহ",
                        "islami_marrage.pdf",
                        "মুফতী মনসূরুল হক দা.বা."
                },
                {
                        "কিতাবুল ইমান",
                        "kitabul_iman.pdf",
                        "মুফতী মনসূরুল হক দা.বা.",
                },
                {
                        "কিতাবুল হাজ্জ",
                        "kitabul_hajj.pdf",
                        "মুফতী মনসূরুল হক দা.বা."

                },
                {
                        "জীবনের শেষ দিন",
                        "jiboner_sesh_din.pdf",
                        "মুফতী মনসূরুল হক দা.বা."
                },
                {
                        "তাবলীগ কী ও কেন",
                        "tablig_ki_o_keno.pdf",
                        "মুফতী মনসূরুল হক দা.বা."
                },
                {
                        "নবীজীর সা. সুন্নাত",
                        "nobijir_sm_sunnah.pdf",
                        "মুফতী মনসূরুল হক দা.বা."
                },
                {
                        "নামায শিক্ষা ইমামদের",
                        "namaj_shikkah_immamder.pdf",
                        "মুফতী মনসূরুল হক দা.বা."
                },
                {
                        "বিপদ-আপদে সান্তনা",
                        "bipod_apoder_shantona.pdf",
                        "মুফতী মনসূরুল হক দা.বা."
                },
                {
                        "বিভিন্ন ধর্ম গ্রন্থে সা",
                        "bibhinno_dhormo_gronthe_sm.pdf",
                        "মুফতী মনসূরুল হক দা.বা."
                },
                {
                        "মউদূদী মতবাদ",
                        "moududi_motobad.pdf",
                        "মুফতী মনসূরুল হক দা.বা."
                },
                {
                        "মালফুযাতে মুযাদ্দিদে দীন",
                        "malfujate_mujaddedin.pdf",
                        "মুফতী মনসূরুল হক দা.বা."
                },
                {
                        "মাসনূন দূ‘আ ও দুরূদ",
                        "masnun_dua_o_durud.pdf",
                        "মুফতী মনসূরুল হক দা.বা."
                },
                {
                        "শরঈ পর্দা",
                        "shar_e_porda.pdf",
                        "মুফতী মনসূরুল হক দা.বা."
                },
                {
                        "সন্তানের শ্রেষ্ট উপহার",
                        "shontaner_shreshto_upohar.pdf",
                        "মুফতী মনসূরুল হক দা.বা."
                },
                {
                        "সন্মিলিত মুনাজাত",
                        "shommilito_munajat.pdf",
                        "মুফতী মনসূরুল হক দা.বা."
                },
                {
                        "হযরত ইউসুফ আ.",
                        "hajrat_usuf_alh.pdf",
                        "মুফতী মনসূরুল হক দা.বা."
                },

                {
                        "তুহফাতুল হাদীস",
                        "tuhfatul_hadis.pdf",
                        "মুফতী মনসূরুল হক দা.বা."
                },
                {
                        "মজলিসে দাওয়াতুল হক",
                        "majlish_e_dawatul_haq.pdf",
                        "মুফতী মনসূরুল হক দা.বা."
                },

                {
                        "মাযহাব ও তাকলীদ",
                        "majhab_o_taklid.pdf",
                        "মুফতী মনসূরুল হক দা.বা."
                },

                {
                        "হাদিয়া আহলুল হাদীস",
                        "hadia_ahalul_hadits.pdf",
                        "মুফতী মনসূরুল হক দা.বা."
                },

                {
                        "হালাল কামাই",
                        "halal_kamai.pdf",
                        "মুফতী মনসূরুল হক দা.বা."
                },

                {
                        "Condolence & Reward of Woes & Adversities",
                        "condolence_reward_of_woes.pdf",
                        "Mufti Mansurul Haq",
                }




        };


        for(int i=0; i<b.length;i++){

            OfflineBook offlineBook = new OfflineBook();

            offlineBook.setBookName(b[i][0]);
            offlineBook.setDownloadString(b[i][1]);
            offlineBook.setBookWriter(" - "+ b[i][2]);

            offlineBookList.add(offlineBook);
            //Log.d(,b[i][1]);
        }


        BookAdapter bookAdapter = new BookAdapter(getContext(),R.layout.offline_book_list_view,offlineBookList);
        offlineBookListView.setAdapter(bookAdapter);

        //Array adapter
       /* ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_list_item_1, android.R.id.text1,offlineBookList );

        offlineBookListView.setAdapter(adapter);*/
    }


    private class BookAdapter extends ArrayAdapter{

        private List<OfflineBook> offlineBooks;
        private int resources;
        private LayoutInflater inflater;
        private TextView bookTitleTV,bookUrl,bookAuthorTV;


        public BookAdapter(Context context, int resource, List <OfflineBook> objects) {
            super(context, resource, objects);

            offlineBooks=objects;
            this.resources=resource;

            inflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @NonNull
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            if(convertView==null){

                convertView=inflater.inflate(resources,null);
            }

            bookTitleTV=(TextView) convertView.findViewById(R.id.offlineBookTitle);
            bookAuthorTV=(TextView) convertView.findViewById(R.id.offlineBookAuthor);
            //bookUrl=(TextView) convertView.findViewById(R.id.offlineBookUrl);




            //Assign
            final String bookTitle = String.valueOf(offlineBooks.get(position).getBookName());
            final String bookURL = String.valueOf(offlineBooks.get(position).getDownloadString());
            final String bookAuthor = String.valueOf(offlineBooks.get(position).getBookWriter());


            bookTitleTV.setText(bookTitle);
            bookAuthorTV.setText(bookAuthor);

            bookTitleTV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CopyFile copyFile = new CopyFile(getContext());
                    copyFile.copyFileToInternalStorage(bookURL,copyFile.OPEN);
                }
            });

            return convertView;
        }

    }


    class OfflineBook{

        public String getBookName() {
            return bookName;
        }

        public void setBookName(String bookName) {
            this.bookName = bookName;
        }

        public String getBookWriter() {
            return bookWriter;
        }

        public void setBookWriter(String bookWriter) {
            this.bookWriter = bookWriter;
        }

        public String getDownloadString() {
            return downloadString;
        }

        public void setDownloadString(String downloadString) {
            this.downloadString = downloadString;
        }

        private String bookName,bookWriter,downloadString;


    }
}
