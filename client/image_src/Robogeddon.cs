using System;
using System.Diagnostics;
using System.Windows.Forms;
using System.IO;

class RobogeddonRunner {
    static void Main() {
        string batFile = Directory.GetCurrentDirectory() + "\\src\\bin\\client.bat";

        if (!File.Exists(batFile)) {
            MessageBox.Show("The launch script could not be found: " + batFile,
                "Critical error", MessageBoxButtons.OK, MessageBoxIcon.Error);
            System.Environment.Exit(42);
        }

        var processInfo = new ProcessStartInfo("cmd.exe", "/c \"" + batFile + "\"");
        processInfo.CreateNoWindow = true;
        processInfo.UseShellExecute = false;
        processInfo.RedirectStandardError = true;
        processInfo.RedirectStandardOutput = true;

        var process = Process.Start(processInfo);

        process.OutputDataReceived += (object sender, DataReceivedEventArgs e) => Console.WriteLine("output>>" + e.Data);
        process.BeginOutputReadLine();

        process.ErrorDataReceived += (object sender, DataReceivedEventArgs e) => Console.WriteLine("error>>" + e.Data);
        process.BeginErrorReadLine();

        process.WaitForExit();

        Console.WriteLine("ExitCode: {0}", process.ExitCode);
        process.Kill();
    }
}