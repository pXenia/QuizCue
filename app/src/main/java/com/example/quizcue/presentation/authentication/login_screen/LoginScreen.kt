package com.example.quizcue.presentation.authentication.login_screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.quizcue.R
import com.example.quizcue.domain.Response
import com.example.quizcue.presentation.tools.AppAlertDialog
import com.example.quizcue.presentation.tools.CircularProgress
import com.example.quizcue.presentation.tools.Screen
import com.google.firebase.auth.AuthResult
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(
    navController: NavController,
    loginViewModel: LoginViewModel = hiltViewModel()
) {
    val hostState = remember {
        SnackbarHostState()
    }
    Scaffold(
        snackbarHost = { SnackbarHost(hostState = hostState) },
        modifier = Modifier.fillMaxSize()
    ) { paddingValues ->
        Content(
            hostState = hostState,
            paddingValues = paddingValues,
            signInStateFlow = loginViewModel.loginFlow,
            resetPasswordStateFlow = loginViewModel.resetPasswordFlow,
            onRegisterNow = { navController.navigate(Screen.Register.route) },
            onForgotPassword = { email -> loginViewModel.resetPassword(email) },
            onLogin = { email, password -> loginViewModel.login(email, password) },
            loginSuccess = {
                navController.navigate(Screen.Home.route) {
                    popUpTo(
                        0
                    )
                }
            }
        )
    }
}

@Composable
fun Content(
    paddingValues: PaddingValues,
    signInStateFlow: MutableSharedFlow<Response<AuthResult>>,
    resetPasswordStateFlow: MutableSharedFlow<Response<Void?>>,
    onRegisterNow: () -> Unit,
    onForgotPassword: (String) -> Unit,
    onLogin: (String, String) -> Unit,
    loginSuccess: () -> Unit,
    hostState: SnackbarHostState
) {
    val context = LocalContext.current

    var emailText by remember {
        mutableStateOf("")
    }
    var passwordText by remember {
        mutableStateOf("")
    }
    var showForgotPasswordDialog by remember {
        mutableStateOf(false)
    }
    val scope = rememberCoroutineScope()

    if (showForgotPasswordDialog)
        AppAlertDialog(
            onDismissRequest = { showForgotPasswordDialog = false },
            onConfirmation = {
                if (emailText != "") {
                    onForgotPassword(emailText)
                    showForgotPasswordDialog = false
                } else {
                    scope.launch {
                        hostState.showSnackbar(context.getString(R.string.add_email))
                    }
                }
            },
            title = stringResource(R.string.forgot_your_password),
            text = stringResource(R.string.reset_password),
            confirmButtonText = stringResource(R.string.send),
            dismissButtonText = stringResource(R.string.cansel),
            cancelable = true
        )
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            text = stringResource(R.string.log_in),
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 25.dp, end = 20.dp, top = 20.dp),
            style = MaterialTheme.typography.displayMedium,
            fontWeight = FontWeight.SemiBold
        )
        Text(
            text = stringResource(R.string.кegister_to_continue),
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 25.dp, end = 20.dp, top = 20.dp),
            style = MaterialTheme.typography.bodyLarge
        )
        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(start = 25.dp, end = 20.dp, top = 20.dp),
            singleLine = true,
            value = emailText,
            onValueChange = { text -> emailText = text },
            label = { Text(text = stringResource(R.string.email)) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            leadingIcon = { Icon(Icons.Filled.Email, null) }
        )
        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(20.dp),
            singleLine = true,
            value = passwordText,
            onValueChange = { text -> passwordText = text },
            label = { Text(stringResource(R.string.password)) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            visualTransformation = PasswordVisualTransformation(),
            leadingIcon = { Icon(Icons.Filled.Lock, null) }
        )
        Button(
            onClick = {
                onLogin(emailText, passwordText)
            },
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(start = 25.dp, end = 20.dp, top = 20.dp),
            content = { Text(text = stringResource(R.string.log_in)) }
        )
        Text(
            color = MaterialTheme.colorScheme.primary,
            text = stringResource(R.string.forgot_your_password),
            modifier = Modifier
                .wrapContentWidth()
                .wrapContentHeight()
                .align(alignment = Alignment.CenterHorizontally)
                .padding(start = 20.dp, end = 20.dp, top = 10.dp)
                .clickable { showForgotPasswordDialog = true },
            style = MaterialTheme.typography.titleMedium
        )
        Spacer(modifier = Modifier.weight(1f))
        Text(
            text = buildAnnotatedString {
                append(stringResource(R.string.dont_have_an_account))
                withStyle(style = SpanStyle(MaterialTheme.colorScheme.primary)) { append(
                    stringResource(R.string.register)
                ) }
            },
            modifier = Modifier
                .wrapContentWidth()
                .wrapContentHeight()
                .align(alignment = Alignment.CenterHorizontally)
                .padding(20.dp)
                .clickable { onRegisterNow() },
            style = MaterialTheme.typography.titleSmall
        )
    }
    LoginInState(
        flow = signInStateFlow,
        onSuccess = { loginSuccess() },
        onError = { scope.launch { hostState.showSnackbar(context.getString(R.string.the_email_address_or_password_is_incorrect)) } }
    )
    ResetPasswordState(
        flow = resetPasswordStateFlow,
        onSuccess = { scope.launch { hostState.showSnackbar(context.getString(R.string.Email_sent_successfully)) } },
        onError = { scope.launch { hostState.showSnackbar(context.getString(R.string.something_went_wrong)) } }
    )
}

@Composable
fun ResetPasswordState(
    flow: MutableSharedFlow<Response<Void?>>,
    onSuccess: () -> Unit,
    onError: () -> Unit
) {
    var isLoading by remember { mutableStateOf(false) }
    if (isLoading) CircularProgress()
    LaunchedEffect(Unit) {
        flow.collect {
            when (it) {
                is Response.Loading -> isLoading = true

                is Response.Error -> {
                    isLoading = false
                    onError()
                }

                is Response.Success -> {
                    isLoading = false
                    onSuccess()
                }
            }
        }
    }
}

@Composable
fun LoginInState(
    flow: MutableSharedFlow<Response<AuthResult>>,
    onSuccess: () -> Unit,
    onError: () -> Unit
) {
    var isLoading by remember { mutableStateOf(false) }
    if (isLoading) CircularProgress()
    LaunchedEffect(Unit) {
        flow.collect() {
            when (it) {
                is Response.Loading -> isLoading = true
                is Response.Error -> {
                    isLoading = false
                    onError()
                }

                is Response.Success -> {
                    isLoading = false
                    onSuccess()
                }
            }
        }
    }
}

