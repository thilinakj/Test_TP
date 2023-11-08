package io.tklabs.testintv.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.tklabs.testintv.ui.theme.TestIntvTheme

class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TestIntvTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background,
                ) {
                    val collectDataAsyncResult by viewModel.collectDataAsyncResult.collectAsState(
                        initial = "",
                    )
                    val collectDataCallbackWayResult by viewModel.collectDataCallbackWayResult.collectAsState(
                        initial = "",
                    )
                    Column(
                        modifier = Modifier.fillMaxSize().padding(8.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Button(onClick = { viewModel.callApiAsyncWay() }) {
                            Text(text = "Collect Data from multiple end points - Async")
                        }
                        Text(
                            text = "Async Way: \n$collectDataAsyncResult",
                            modifier = Modifier.fillMaxWidth(),
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(onClick = { viewModel.callApiCallbackWay() }) {
                            Text(text = "Collect Data from multiple end points - Callback")
                        }
                        Text(
                            text = "Callback Way: \n$collectDataCallbackWayResult",
                            modifier = Modifier.fillMaxWidth(),
                        )
                    }
                }
            }
        }
    }
}
