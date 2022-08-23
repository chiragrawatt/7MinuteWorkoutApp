package com.example.a7minuteworkoutapp

import android.icu.math.BigDecimal
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.a7minuteworkoutapp.databinding.FragmentBMIBinding
import java.math.RoundingMode

class BMIFragment : Fragment() {

    private lateinit var binding: FragmentBMIBinding

    companion object {
        private const val METRIC_UNITS_VIEW = "METRIC_UNIT_VIEW" // Metric Unit View
        private const val US_UNITS_VIEW = "US_UNIT_VIEW" // US Unit View
    }

    private var currentVisibleView: String = METRIC_UNITS_VIEW

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBMIBinding.inflate(layoutInflater)

        binding.rgUnits.setOnCheckedChangeListener { group, checkedId ->
            if (checkedId == R.id.rb_metric_units) {
                makeVisibleMetricUnitsView()
            } else {
                makeVisibleUsUnitsView()
            }
        }

        binding.btnBmiCalculate.setOnClickListener {
            if (currentVisibleView == METRIC_UNITS_VIEW) {
                // The values are validated.
                if (validateMetricUnits()) {

                    // The height value is converted to float value and divided by 100 to convert it to meter.
                    val heightValue: Float = binding.etMetricUnitHeight.text.toString().toFloat() / 100

                    // The weight value is converted to float value
                    val weightValue: Float = binding.etMetricUnitWeight.text.toString().toFloat()

                    // BMI value is calculated in METRIC UNITS using the height and weight value.
                    val bmi = weightValue / (heightValue * heightValue)

                    displayBMIResult(bmi)
                } else {
                    Toast.makeText(
                        context,
                        "Please enter valid values.",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }
            } else {

                // The values are validated.
                if (validateUsUnits()) {

                    val usUnitHeightValueFeet: String =
                        binding.etFtUnitHeight.text.toString() // Height Feet value entered in EditText component.
                    val usUnitHeightValueInch: String =
                        binding.etInchUnitHeight.text.toString() // Height Inch value entered in EditText component.
                    val usUnitWeightValue: Float = binding.etUsUnitWeight.text.toString()
                        .toFloat() // Weight value entered in EditText component.

                    // Here the Height Feet and Inch values are merged and multiplied by 12 for converting it to inches.
                    val heightValue =
                        usUnitHeightValueInch.toFloat() + usUnitHeightValueFeet.toFloat() * 12

                    // This is the Formula for US UNITS result.
                    // Reference Link : https://www.cdc.gov/healthyweight/assessing/bmi/childrens_bmi/childrens_bmi_formula.html
                    val bmi = 703 * (usUnitWeightValue / (heightValue * heightValue))

                    displayBMIResult(bmi) // Displaying the result into UI
                } else {
                    Toast.makeText(
                        context,
                        "Please enter valid values.",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }
            }
        }

        return binding.root
    }

    private fun displayBMIResult(bmi: Float) {
        val bmiLabel: String
        val bmiDescription: String

        if (java.lang.Float.compare(bmi, 15f) <= 0) {
            bmiLabel = "Very severely underweight"
            bmiDescription = "Oops! You really need to take care of your better! Eat more!"
        } else if (java.lang.Float.compare(bmi, 15f) > 0 && java.lang.Float.compare(
                bmi,
                16f
            ) <= 0
        ) {
            bmiLabel = "Severely underweight"
            bmiDescription = "Oops! You really need to take care of your better! Eat more!"
        } else if (java.lang.Float.compare(bmi, 16f) > 0 && java.lang.Float.compare(
                bmi,
                18.5f
            ) <= 0
        ) {
            bmiLabel = "Underweight"
            bmiDescription = "Oops! You really need to take care of your better! Eat more!"
        } else if (java.lang.Float.compare(bmi, 18.5f) > 0 && java.lang.Float.compare(
                bmi,
                25f
            ) <= 0
        ) {
            bmiLabel = "Normal"
            bmiDescription = "Congratulations! You are in a good shape!"
        } else if (java.lang.Float.compare(bmi, 25f) > 0 && java.lang.Float.compare(
                bmi,
                30f
            ) <= 0
        ) {
            bmiLabel = "Overweight"
            bmiDescription = "Oops! You really need to take care of your yourself! Workout maybe!"
        } else if (java.lang.Float.compare(bmi, 30f) > 0 && java.lang.Float.compare(
                bmi,
                35f
            ) <= 0
        ) {
            bmiLabel = "Obese Class | (Moderately obese)"
            bmiDescription = "Oops! You really need to take care of your yourself! Workout maybe!"
        } else if (java.lang.Float.compare(bmi, 35f) > 0 && java.lang.Float.compare(
                bmi,
                40f
            ) <= 0
        ) {
            bmiLabel = "Obese Class || (Severely obese)"
            bmiDescription = "OMG! You are in a very dangerous condition! Act now!"
        } else {
            bmiLabel = "Obese Class ||| (Very Severely obese)"
            bmiDescription = "OMG! You are in a very dangerous condition! Act now!"
        }

        binding.tvYourBmi.visibility = View.VISIBLE
        binding.tvBmiType.visibility = View.VISIBLE
        binding.tvBmiValue.visibility = View.VISIBLE
        binding.tvBmiDescription.visibility = View.VISIBLE

        val bmiValue = java.math.BigDecimal(bmi.toDouble()).setScale(2, RoundingMode.HALF_EVEN).toString()

        binding.tvBmiValue.text = bmiValue
        binding.tvBmiType.text = bmiLabel
        binding.tvBmiDescription.text = bmiDescription

    }

        private fun makeVisibleMetricUnitsView() {
            currentVisibleView = METRIC_UNITS_VIEW // Current View is updated here.
            binding.llMetricInput.visibility = View.VISIBLE // METRIC UNITS VIEW is Visible
            binding.llUsInput.visibility = View.GONE // US UNITS VIEW is hidden

            binding.etMetricUnitHeight.text!!.clear() // height value is cleared if it is added.
            binding.etMetricUnitWeight.text!!.clear() // weight value is cleared if it is added.

            binding.tvYourBmi.visibility = View.INVISIBLE // Result is cleared and the labels are hidden
            binding.tvBmiValue.visibility = View.INVISIBLE // Result is cleared and the labels are hidden
            binding.tvBmiType.visibility = View.INVISIBLE // Result is cleared and the labels are hidden
            binding.tvBmiDescription.visibility = View.INVISIBLE // Result is cleared and the labels are hidden
        }

        /**
         * Function is used to make visible the US UNITS VIEW and hide the METRIC UNITS VIEW.
         */
        private fun makeVisibleUsUnitsView() {
            currentVisibleView = US_UNITS_VIEW // Current View is updated here.
            binding.llMetricInput.visibility = View.GONE // METRIC UNITS VIEW is hidden
            binding.llUsInput.visibility = View.VISIBLE // US UNITS VIEW is Visible

            binding.etUsUnitWeight.text!!.clear() // weight value is cleared if it is added.
            binding.etFtUnitHeight.text!!.clear() // height feet value is cleared if it is added.
            binding.etInchUnitHeight.text!!.clear() // height inch is cleared if it is added.

            binding.tvYourBmi.visibility = View.INVISIBLE // Result is cleared and the labels are hidden
            binding.tvBmiValue.visibility = View.INVISIBLE // Result is cleared and the labels are hidden
            binding.tvBmiType.visibility = View.INVISIBLE // Result is cleared and the labels are hidden
            binding.tvBmiDescription.visibility = View.INVISIBLE // Result is cleared and the labels are hidden
        }
        // END

        /**
         * Function is used to validate the input values for METRIC UNITS.
         */
        private fun validateMetricUnits(): Boolean {
            var isValid = true

            if (binding.etMetricUnitWeight.text.toString().isEmpty()) {
                isValid = false
            } else if (binding.etMetricUnitHeight.text.toString().isEmpty()) {
                isValid = false
            }

            return isValid
        }

        // TODO(Step 1 : Validating the US UNITS view input values.)
        // START
        /**
         * Function is used to validate the input values for US UNITS.
         */
        private fun validateUsUnits(): Boolean {
            var isValid = true

            if (binding.etUsUnitWeight.text.toString().isEmpty()) {
                isValid = false
            } else if (binding.etUsUnitWeight.text.toString().isEmpty()) {
                isValid = false
            } else if (binding.etUsUnitWeight.text.toString().isEmpty()) {
                isValid = false
            }

            return isValid
        }
}