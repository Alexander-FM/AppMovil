package com.example.comisariaapp.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;

import com.example.comisariaapp.R;
import com.example.comisariaapp.fragments.ConsultarDenunciaFragment;
import com.example.comisariaapp.fragments.ConsultarTramiteFragment;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class ConsultarTramitesDenunciasActivity extends AppCompatActivity {

    private ViewPager view_pager_consultar_denuncias_tramites;
    private TabLayout tab_layout_consultar_denuncias_tramites;
    private ConsultarDenunciaFragment consultarDenunciaFragment;
    private ConsultarTramiteFragment consultarTramiteFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consultar_tramites_denuncias);
        Toolbar toolbar = this.findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.atras);
        toolbar.setNavigationOnClickListener(v -> {//Reemplazo con lamba
            this.startActivity(new Intent(getApplicationContext(), MenuActivity.class));
            this.overridePendingTransition(R.anim.rigth_in, R.anim.rigth_out);
        });

        view_pager_consultar_denuncias_tramites = findViewById(R.id.view_pager_consultar_denuncias_tramites);
        tab_layout_consultar_denuncias_tramites = findViewById(R.id.tab_layout_consultar_denuncias_tramites);
        consultarDenunciaFragment = new ConsultarDenunciaFragment();
        consultarTramiteFragment = new ConsultarTramiteFragment();

        tab_layout_consultar_denuncias_tramites.setupWithViewPager(view_pager_consultar_denuncias_tramites);

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), 0);
        viewPagerAdapter.addFragment(consultarDenunciaFragment, "Consultar Denuncia");
        viewPagerAdapter.addFragment(consultarTramiteFragment, "Consultar Trámite");

        view_pager_consultar_denuncias_tramites.setAdapter(viewPagerAdapter);
        tab_layout_consultar_denuncias_tramites.getTabAt(0).setIcon(R.drawable.ic_assignment);
        tab_layout_consultar_denuncias_tramites.getTabAt(1).setIcon(R.drawable.ic_assignment);
    }

    private class ViewPagerAdapter extends FragmentPagerAdapter {
        private List<Fragment> fragments = new ArrayList<>();
        private List<String> fragmentTitle = new ArrayList<>();

        public ViewPagerAdapter(@NonNull FragmentManager fm, int behavior) {
            super(fm, behavior);
        }

        public void addFragment(Fragment fragment, String title) {
            fragments.add(fragment);
            fragmentTitle.add(title);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return fragmentTitle.get(position);
        }
    }
}