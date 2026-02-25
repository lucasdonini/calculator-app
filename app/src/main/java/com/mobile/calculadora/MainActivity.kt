package com.mobile.calculadora

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mobile.calculadora.ui.theme.CalculadoraTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val viewModel: CalculatorViewModel = viewModel()
            CalculadoraTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Surface(
                        modifier = Modifier.fillMaxSize()
                    ) { Layout(viewModel = viewModel, innerPadding = innerPadding) }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun Preview() {
    val viewModel: CalculatorViewModel = viewModel()
    CalculadoraTheme(darkTheme = true, dynamicColor = false) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) { Layout(viewModel = viewModel) }
    }
}

@Composable
fun Layout(viewModel: CalculatorViewModel, innerPadding: PaddingValues = PaddingValues()) = Column(
    modifier = Modifier.fillMaxSize(),
    verticalArrangement = Arrangement.Bottom
) {
    val result by viewModel.result
    val operation by viewModel.operationPreview

    OperationField(operation = operation)
    ResultField(result = result)
    Keyboard(viewModel = viewModel, innerPadding = innerPadding)
}

@Composable
fun OperationField(operation: String): Unit = Text(
    text = operation,
    modifier = Modifier.fillMaxWidth(),
    textAlign = TextAlign.Right,
    fontSize = 30.sp,
    color = MaterialTheme.colorScheme.onSurfaceVariant,
)

@Composable
fun KeyboardRow(content: @Composable (RowScope.() -> Unit)) = Row(
    modifier = Modifier.fillMaxWidth(),
    verticalAlignment = Alignment.CenterVertically,
    horizontalArrangement = Arrangement.SpaceAround,
    content = content
)

@Composable
fun Keyboard(viewModel: CalculatorViewModel, innerPadding: PaddingValues = PaddingValues()) {
    val density = LocalDensity.current
    val screenHeight = with(density) { LocalWindowInfo.current.containerSize.height.toDp() }

    Column(
        modifier = Modifier
            .height(screenHeight * 0.6f)
            .padding(bottom = innerPadding.calculateBottomPadding()),
        verticalArrangement = Arrangement.SpaceAround,
    ) {
        KeyboardRow {
            CommandButton(text = "<-") { viewModel.backspace() }
            CommandButton(text = "AC") { viewModel.clear() }
            CommandButton(text = "%") { }
            OperationButton(text = "/") { viewModel.type('/') }
        }

        KeyboardRow {
            listOf('7', '8', '9').forEach { CustomButton(text = "$it") { viewModel.type(it) } }
            OperationButton(text = "*") { viewModel.type('*') }
        }

        KeyboardRow {
            listOf('4', '5', '6').forEach { CustomButton(text = "$it") { viewModel.type(it) } }
            OperationButton(text = "-") { viewModel.type('-') }
        }

        KeyboardRow {
            listOf('1', '2', '3').forEach { CustomButton(text = "$it") { viewModel.type(it) } }
            OperationButton(text = "+") { viewModel.type('+') }
        }

        KeyboardRow {
            CustomButton(text = "+/-") { viewModel.type('-') }
            listOf('0', ',').forEach { CustomButton(text = "$it") { viewModel.type(it) }}
            OperationButton(text = "=") { viewModel.solve() }
        }
    }
}

@Composable
fun OperationButton(text: String, onClick: () -> Unit) = CustomButton(
    text = text,
    backgroundColor = MaterialTheme.colorScheme.secondary,
    textColor = MaterialTheme.colorScheme.onSecondary,
    onClick = onClick
)

@Composable
fun CommandButton(text: String, onClick: () -> Unit) = CustomButton(
    text = text,
    backgroundColor = MaterialTheme.colorScheme.tertiary,
    textColor = MaterialTheme.colorScheme.onTertiary,
    onClick = onClick
)

@Composable
fun CustomButton(
    text: String,
    backgroundColor: Color = MaterialTheme.colorScheme.primary,
    textColor: Color = MaterialTheme.colorScheme.onPrimary,
    onClick: () -> Unit,
) {
    val density = LocalDensity.current
    val containerSize = LocalWindowInfo.current.containerSize
    val screenHeight = with(density) { containerSize.height.toDp() }
    val screenWidth = with(density) { containerSize.width.toDp() }

    TextButton(
        modifier = Modifier
            .width(screenWidth / 5)
            .height(screenHeight / 10)
            .background(
                color = backgroundColor,
                shape = RoundedCornerShape(percent = 100)
            ),
        onClick = onClick
    ) {
        Text(
            text = text,
            color = textColor,
            fontSize = 30.sp
        )
    }
}

@Composable
fun ResultField(result: String, modifier: Modifier = Modifier) {
    Text(
        text = result,
        color = MaterialTheme.colorScheme.onSurface,
        modifier = modifier.fillMaxWidth(),
        fontSize = 68.sp,
        textAlign = TextAlign.Right
    )
}