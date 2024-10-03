package com.example.mycontractorcalculator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.mycontractorcalculator.ui.theme.MyContractorCalculatorTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyContractorCalculatorTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    CalculatorScreen(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun CalculatorScreen(modifier: Modifier = Modifier) {
    var laborCost by remember { mutableStateOf("") }
    var materialCost by remember { mutableStateOf("") }
    var subTotal by remember { mutableStateOf("") }
    var tax by remember { mutableStateOf("") }
    var total by remember { mutableStateOf("") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Labor Cost Row
        CostInputRow(label = "Labor:", value = laborCost, onValueChange = { laborCost = it })
        Spacer(modifier = Modifier.height(16.dp))

        // Material Cost Row
        CostInputRow(label = "Materials:", value = materialCost, onValueChange = { materialCost = it })
        Spacer(modifier = Modifier.height(16.dp))

        // Calculate Button
        Button(
            onClick = {
                // Call the calculateCosts function when the button is clicked
                val (subTotalValue, taxValue, totalValue) = calculateCosts(laborCost, materialCost)
                subTotal = String.format("%.2f", subTotalValue)
                tax = String.format("%.2f", taxValue)
                total = String.format("%.2f", totalValue)
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = androidx.compose.ui.graphics.Color.Transparent,
                contentColor = androidx.compose.ui.graphics.Color(0xFF4169E1)
            )
        ) {
            Text("Calculate")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Results
        ResultRow(label = "SubTotal:", value = subTotal)
        Spacer(modifier = Modifier.height(4.dp))
        ResultRow(label = "Tax:", value = tax)
        Spacer(modifier = Modifier.height(4.dp))
        ResultRow(label = "Total:", value = total)
    }
}

fun calculateCosts(laborCost: String, materialCost: String): Triple<Double, Double, Double> {
    val labor = laborCost.toDoubleOrNull() ?: 0.0
    val material = materialCost.toDoubleOrNull() ?: 0.0

    // Calculate subtotal, tax, and total
    val subTotalValue = labor + material
    val taxValue = subTotalValue * TAX_RATE
    val totalValue = subTotalValue + taxValue

    return Triple(subTotalValue, taxValue, totalValue)
}

@Composable
fun CostInputRow(label: String, value: String, onValueChange: (String) -> Unit) {
    Row(
        modifier = Modifier
            .width(320.dp) // Keep the fixed width for the row
            .height(28.dp), // Set a specific height for the row
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            modifier = Modifier
                .weight(1f)
                .padding(end = 8.dp),
            textAlign = TextAlign.End
        )
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            label = { Text("") },
            modifier = Modifier
                .weight(2f) // Fill remaining space
                .height(40.dp), // Set a specific height
        )
    }
}

@Composable
fun ResultRow(label: String, value: String) {
    Row(modifier = Modifier.width(300.dp), verticalAlignment = Alignment.CenterVertically) {
        Text(
            text = label,
            modifier = Modifier.weight(1f).padding(end = 8.dp),
            textAlign = TextAlign.End // Right align
        )
        Text(text = value, modifier = Modifier.weight(1f), textAlign = TextAlign.Start)
    }
}

private const val TAX_RATE = 0.05

@Preview(showBackground = true)
@Composable
fun CalculatorScreenPreview() {
    MyContractorCalculatorTheme {
        CalculatorScreen()
    }
}
