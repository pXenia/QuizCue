import com.example.quizcue.presentation.homescreen.HomeScreenViewModel

import android.annotation.SuppressLint
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoGraph
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.QuestionMark
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color.Companion.Transparent
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.quizcue.R
import com.example.quizcue.domain.model.Course
import com.example.quizcue.domain.model.User
import com.example.quizcue.presentation.elements.CourseCard
import com.example.quizcue.presentation.tools.Screen

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun HomeScreen(
    navController: NavController,
    homeViewModel: HomeScreenViewModel = hiltViewModel()) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {},
                colors = TopAppBarDefaults.topAppBarColors(Transparent),
                navigationIcon = {
                    IconButton(onClick = {
                        homeViewModel.logout()
                        navController.navigate(Screen.Login.route) { popUpTo(0) }
                    }) {
                        Icon(
                            imageVector = Icons.Filled.Menu,
                            contentDescription = "Menu"
                        )
                    }
                },
            )
        },
        modifier = Modifier
            .padding(
                top = WindowInsets.statusBars
                    .asPaddingValues()
                    .calculateTopPadding()
            )
            .background(MaterialTheme.colorScheme.surfaceContainerHigh),
        containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
    ) {
        MainPreview(navController)
    }
}

@Composable
fun MainPreview(
    navController: NavController,
    homeViewModel: HomeScreenViewModel = hiltViewModel()
) {
    val user by homeViewModel.uiState.collectAsState()
    val email = homeViewModel.email

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(horizontal = 30.dp)
    ) {
        Header(user, email)
        Body(user, navController)
        CourseCard(
            navController = navController,
            textColor = MaterialTheme.colorScheme.onPrimary,
            trackColor = MaterialTheme.colorScheme.tertiary,
            cardColor = CardDefaults.cardColors(MaterialTheme.colorScheme.primary),
            progress = 0.55f,
            course = Course("","","")
        )
    }
}

@Composable
fun Header(
    user: User,
    email: String,
    modifier: Modifier = Modifier,
)
{
    Column(
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight(0.35f),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            modifier = Modifier
                .padding(top = 15.dp)
                .size(140.dp)
                .border(
                    BorderStroke(1.dp, MaterialTheme.colorScheme.tertiary),
                    CircleShape
                )
                .clip(CircleShape),
            painter = if (user.photo != null)
                BitmapPainter(user.photo!!.asImageBitmap())
            else
                painterResource(id = R.drawable.koshka),
            contentDescription = "user",
            contentScale = ContentScale.Crop,
            colorFilter = ColorFilter.colorMatrix(ColorMatrix().apply { setToSaturation(0.5f) })
        )
        Text(
            text = user.name,
            style = MaterialTheme.typography.headlineMedium
        )
        Text(
            text = email,
            style = MaterialTheme.typography.bodySmall
        )
    }
}

@Composable
fun Body(
    user: User,
    navController: NavController) {
    val activeItems = listOf(
        listOf(Icons.Rounded.Favorite, "Избранное", "Всего вопросов: 5"),
        listOf(Icons.Filled.QuestionMark, "Все вопросы", "Всего вопросов: 15"),
        listOf(Icons.Filled.AutoGraph, "Статистика", "Ваш прогресс!")
    )
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.50f)
            .border(1.dp, MaterialTheme.colorScheme.tertiary, FloatingActionButtonDefaults.shape)
            .padding(vertical = 20.dp, horizontal = 25.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .align(Alignment.Center),
            verticalArrangement = Arrangement.SpaceBetween,
        ) {
            activeItems.forEach { item ->
                ActiveItem(item[0] as ImageVector, item[1].toString(), item[2].toString(),
                    onClick = {
                        if (item == activeItems.last()) {
                            navController.navigate(Screen.Competition.route + "?competitionId=${user.competitionId}")
                        } else {
                            navController.navigate(Screen.Quiz.route)
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun ActiveItem(
    icon: ImageVector,
    title: String,
    subtitle: String,
    onClick: () -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Absolute.SpaceBetween,
    ) {
        FloatingActionButton(
            modifier = Modifier.border(
                1.dp,
                MaterialTheme.colorScheme.tertiary,
                FloatingActionButtonDefaults.shape
            ),
            onClick = onClick,
            containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
            contentColor = MaterialTheme.colorScheme.primary
        ) {
            Icon(imageVector = icon, contentDescription = null)
        }
        Spacer(modifier = Modifier.fillMaxWidth(0.20f))
        Column {
            Text(text = title, style = MaterialTheme.typography.titleMedium)
            Text(text = subtitle, style = MaterialTheme.typography.titleSmall)
        }
    }
}
