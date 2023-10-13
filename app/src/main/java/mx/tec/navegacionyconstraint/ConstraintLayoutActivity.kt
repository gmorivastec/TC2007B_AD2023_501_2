package mx.tec.navegacionyconstraint

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import mx.tec.navegacionyconstraint.ui.theme.NavegacionYConstraintTheme

class ConstraintLayoutActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NavegacionYConstraintTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    DecoupledConstraintExample()
                }
            }
        }
    }
}

@Composable
fun ConstraintLayoutExample() {

    // para hacer constraint layout utilizamos un composable que se llama
    // ConstraintLayout! el uso es similar a column / row
    ConstraintLayout {

        // 1era cosa nueva -
        // para poder hacer un layout con constraint layout necesitamos referencias
        val(button, text1, text2) = createRefs()

        Button(
            onClick = {
                Log.wtf("TEST", "JUST TESTING")
            },
            // aquí es donde establecemos referencia para este elemento
            // y relación con los demás elementos en pantalla
            modifier = Modifier.constrainAs(button){

                // aquí es donde los van los constraints!!!
                // establecemos relaciones con los otros objetos
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                top.linkTo(parent.top, margin = 30.dp)
                // bottom
            }
        ){
            Text("JUST A BUTTON")
        }

        Text(
            "TEXT 1",
            modifier = Modifier.constrainAs(text1){
                top.linkTo(button.bottom, margin = 15.dp)
                end.linkTo(button.start, margin = 10.dp)
            }
        )

        Text(
            "TEXT 2",
            modifier = Modifier.constrainAs(text2){
                end.linkTo(parent.end, margin = 20.dp)
                bottom.linkTo(parent.bottom, margin = 20.dp)
            }

        )
    }


}

// decoupled constraints
// podemos definir constraints por separado
// y luego utlizarlas en un layout específico
private fun myConstraints(leftMargin : Dp, topMargin : Dp) : ConstraintSet {
    return ConstraintSet {

        val button1 = createRefFor("button1")
        val button2 = createRefFor("button2")
        val button3 = createRefFor("button3")

        constrain(button1) {
            top.linkTo(parent.top, margin = topMargin)
            start.linkTo(parent.start, margin = leftMargin)
        }

        constrain(button2) {
            top.linkTo(button1.bottom, margin = topMargin)
            start.linkTo(button1.end, margin = leftMargin)
        }

        constrain(button3) {
            top.linkTo(button2.bottom, margin = topMargin)
            end.linkTo(button2.start, margin = leftMargin)
        }
    }
}

// el constraint set solito no muestra nada
// sólo establecemos relaciones entre referencias (Que no son composables)
@Composable
fun DecoupledConstraintExample() {

    BoxWithConstraints {
        val constraints = myConstraints(10.dp, 10.dp)
        ConstraintLayout (constraints) {
            Button(
                onClick = { /*TODO*/ },
                modifier = Modifier.layoutId("button1")
            ) {
                Text("Button 1")
            }

            Button(
                onClick = { /*TODO*/ },
                modifier = Modifier.layoutId("button2")
            ) {
                Text("Button 2")
            }

            Button(
                onClick = { /*TODO*/ },
                modifier = Modifier.layoutId("button3")
            ) {
                Text("Button 3")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview2() {
    NavegacionYConstraintTheme {
        ConstraintLayoutExample()
    }
}