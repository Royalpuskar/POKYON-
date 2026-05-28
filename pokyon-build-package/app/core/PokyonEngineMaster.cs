using UnityEngine;
using UnityEngine.UI;
using System;

[RequireComponent(typeof(CharacterController))]
public class PokyonEngineMaster : MonoBehaviour
{
    public CharacterController controller;
    public Transform playerCamera;
    public float walkSpeed = 6.0f;
    public float sprintSpeed = 11.0f;
    private float activeSpeed;

    public Text uiTimerDisplay;
    public GameObject lockoutCanvasOverlay;
    private float cumulativePlaytime = 0f;
    private float activeSessionTime = 0f;

    void Start()
    {
        activeSpeed = walkSpeed;
        if (controller == null) controller = GetComponent<CharacterController>();
        if (lockoutCanvasOverlay != null) lockoutCanvasOverlay.SetActive(false);
    }

    void Update()
    {
        if (cumulativePlaytime >= 14400f) {
            if (lockoutCanvasOverlay != null) lockoutCanvasOverlay.SetActive(true);
            if (uiTimerDisplay != null) uiTimerDisplay.text = "DAILY 4-HOUR LIMIT EXCEEDED. SYSTEM LOCKED.";
            return;
        }

        activeSessionTime += Time.deltaTime;
        cumulativePlaytime += Time.deltaTime;

        if (activeSessionTime >= 1800f) {
            if (lockoutCanvasOverlay != null) lockoutCanvasOverlay.SetActive(true);
            if (uiTimerDisplay != null) uiTimerDisplay.text = "30-MINUTE REST MANDATED. STAND UP AND BREAK.";
            if (activeSessionTime >= 3600f) activeSessionTime = 0f; // Soft auto-reset after cooldown
            return;
        }

        float h = Input.GetAxisRaw("Horizontal");
        float v = Input.GetAxisRaw("Vertical");
        Vector3 dir = new Vector3(h, 0f, v).normalized;

        if (Input.GetKey(KeyCode.LeftShift)) activeSpeed = sprintSpeed;
        else activeSpeed = walkSpeed;

        if (dir.magnitude >= 0.1f) {
            float targetAngle = Mathf.Atan2(dir.x, dir.z) * Mathf.Rad2Deg + playerCamera.eulerAngles.y;
            Vector3 moveDir = Quaternion.Euler(0f, targetAngle, 0f) * Vector3.forward;
            controller.Move(moveDir.normalized * activeSpeed * Time.deltaTime);
        }
    }
}
