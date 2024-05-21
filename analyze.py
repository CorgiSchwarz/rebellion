import pandas as pd
import matplotlib.pyplot as plt

def analyze(file_path):
    df = pd.read_csv(file_path)

    quiet = df['quiet']
    jailed = df['jailed']
    active = df['active']

    plt.figure(figsize=(10, 6))

    plt.plot(quiet, label='quiet')
    plt.plot(jailed, label='jailed')
    plt.plot(active, label='active')

    plt.legend()

    plt.title('Changes of quiet, jailed, and active')
    plt.xlabel('Tick')
    plt.ylabel('Count')

    plt.grid(True)
    plt.show()

if __name__ == '__main__':
    analyze("./result/agent.csv")