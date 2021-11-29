package com.example.mobilepaindiary;

import android.os.Bundle;
import android.view.View;
import com.example.mobilepaindiary.R;
import com.example.mobilepaindiary.data.LoginRepository;
import com.example.mobilepaindiary.databinding.ActivityMainBinding;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.appcompat.app.AppCompatActivity;

import static com.example.mobilepaindiary.ui.home.fragment.WeatherTask2Fragment.isIgnoringBatteryOptimizations;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private AppBarConfiguration mAppBarConfiguration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        isIgnoringBatteryOptimizations(this);
        setSupportActionBar(binding.appBar.toolbar);
        // 导航控制器，找到它所控制的fragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        NavHostFragment navHostFragment = (NavHostFragment)
                fragmentManager.findFragmentById(R.id.nav_host_fragment);
        NavController navController = navHostFragment.getNavController();
        // 简易写法
//        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_weather_fragment,R.id.nav_home_fragment, R.id.nav_add_fragment, R.id.nav_view_fragment,R.id.nav_map)
                .setDrawerLayout(binding.drawerLayout)
                .build();

        // navController.getGraph() 可以在toolbar上自动显示返回箭头
//        mAppBarConfiguration = new AppBarConfiguration.Builder(
//                navController.getGraph())
//                .setDrawerLayout(binding.drawerLayout)
//                .build();

        //Sets up a NavigationView for use with a NavController.
        NavigationUI.setupWithNavController(binding.navView, navController);

        //Sets up a Toolbar for use with a NavController.
        //绑定当前的ActionBar，除此之外NavigationUI还能绑定Toolbar和CollapsingToolbarLayout
        //绑定后，系将所切换统会默认处理ActionBar左上角区域，为你添加返回按钮，到的Fragment在导航图里的name属性中的内容显示到Title
        //.setDrawerLayout(drawerLayout)后才会出现菜单按钮
        NavigationUI.setupWithNavController(binding.appBar.toolbar, navController,
                mAppBarConfiguration);
    }

    @Override
    public void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        //Toolbar 必须在onCreate()之后设置标题文本，否则默认标签将覆盖我们的设置
        if (binding.appBar.toolbar != null && LoginRepository.getInstance().getLoggedInUser() != null) {
            binding.appBar.toolbar.setTitle(getString(R.string.app_name));

            binding.appBar.toolbar.setSubtitle("Hello "+ LoginRepository.getInstance().getLoggedInUser().getDisplayName());

        }
    }

}