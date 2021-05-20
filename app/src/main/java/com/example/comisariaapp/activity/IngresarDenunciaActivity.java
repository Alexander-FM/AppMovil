package com.example.comisariaapp.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;

import com.example.comisariaapp.R;
import com.example.comisariaapp.fragments.AgraviadosFragment;
import com.example.comisariaapp.fragments.DenunciaFragment;
import com.example.comisariaapp.fragments.DenunciadosFragment;
import com.example.comisariaapp.fragments.DetalleDenunciaFragment;
import com.example.comisariaapp.utils.DenunciaManager;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class IngresarDenunciaActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private TabLayout tabLayout;
    private DenunciaFragment denunciaFragment;
    private AgraviadosFragment agraviadosFragment;
    private DenunciadosFragment denunciadosFragment;
    private DetalleDenunciaFragment detalleDenunciaFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingresar_denuncia);
        Toolbar toolbar = this.findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.atras);
        toolbar.setNavigationOnClickListener(v -> {//Reemplazo con lamba
            this.startActivity(new Intent(getApplicationContext(), MenuActivity.class));
            this.overridePendingTransition(R.anim.down_in, R.anim.down_out);
        });

        viewPager = findViewById(R.id.view_pager);
        tabLayout = findViewById(R.id.tab_layout);
        denunciaFragment = new DenunciaFragment();
        agraviadosFragment = new AgraviadosFragment();
        denunciadosFragment = new DenunciadosFragment();
        detalleDenunciaFragment = new DetalleDenunciaFragment();
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);

        tabLayout.setupWithViewPager(viewPager);

        viewPagerAdapter.addFragment(denunciaFragment, "Denuncia");
        viewPagerAdapter.addFragment(agraviadosFragment, "Agraviados");
        viewPagerAdapter.addFragment(denunciadosFragment, "Denunciados");
        viewPagerAdapter.addFragment(detalleDenunciaFragment, "Resumen");

        viewPager.setAdapter(viewPagerAdapter);

        tabLayout.getTabAt(0).setIcon(R.drawable.ic_baseline_border_color_24);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_baseline_group_add_24);
        tabLayout.getTabAt(2).setIcon(R.drawable.ic_grupo_denunciados);
        tabLayout.getTabAt(3).setIcon(R.drawable.ic_assigmente);
        /*BadgeDrawable badgeDrawable = tabLayout.getTabAt(0).getOrCreateBadge();
        badgeDrawable.setVisible(true);
        badgeDrawable.setNumber(7);*/
    }

    public class ViewPagerAdapter extends FragmentPagerAdapter {

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